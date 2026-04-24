package com.hospital.billing.service;

import com.hospital.billing.dto.*;
import com.hospital.billing.model.*;
import com.hospital.billing.repository.BillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE LAYER — Billing System (MVC: Model layer).
 *
 * ─── Design Pattern: Strategy (GoF Behavioural) ──────────────────────────────
 *
 *   Discount calculation is FULLY delegated to {@link DiscountStrategy} implementations:
 *     • {@link NoDiscountStrategy}          — 0 % (standard patient)
 *     • {@link InsuranceDiscountStrategy}   — 30 % (insurance-covered)
 *     • {@link SeniorCitizenDiscountStrategy} — 15 % (age ≥ 60, no insurance)
 *
 *   This service selects the right strategy based on the bill request (SELECTION LOGIC),
 *   then calls {@code strategy.apply(gross)} — it does NOT implement any formula itself.
 *
 *   Adding a new discount type (e.g., StaffDiscount) requires:
 *     (a) A new {@link DiscountStrategy} implementation — nothing else changes here.
 *
 * ─── Design Principles ──────────────────────────────────────────────────────
 *
 * • Single Responsibility Principle (SRP):
 *   - Strategy classes own their discount algorithms.
 *   - This service orchestrates the billing lifecycle (generate → issue → pay → cancel).
 *   - The discount formula is NOT the responsibility of BillingService.
 *
 * • Open/Closed Principle (OCP):
 *   - BillingService is CLOSED for modification.
 *   - It is OPEN for extension via new DiscountStrategy implementations.
 *
 * • Dependency Inversion Principle (DIP):
 *   - Depends on {@link BillRepository} (interface) and {@link DiscountStrategy}
 *     (interface) — never on concrete implementations.
 *
 * ─── GRASP Principles ───────────────────────────────────────────────────────
 *
 * • Information Expert:
 *   - This service knows the billing lifecycle rules (DRAFT → PENDING → PAID/CANCELLED).
 *
 * • Controller (GRASP):
 *   - BillingController delegates all business decisions to this service.
 *   - This service is the system-level controller for billing operations.
 *
 * • Low Coupling:
 *   - Only depends on BillRepository and the DiscountStrategy abstraction.
 *
 * • High Cohesion:
 *   - Every method relates exclusively to bill management.
 */
@Service
@Transactional
public class BillingService {

    private final BillRepository billRepo;

    /** DIP: depend on the repository abstraction injected by Spring. */
    public BillingService(BillRepository billRepo) { this.billRepo = billRepo; }

    // ── Strategy Selection (private helper) ────────────────────────────────

    /**
     * STRATEGY PATTERN — selects the appropriate {@link DiscountStrategy} based on
     * the patient's insurance status and age.
     *
     * Selection priority:
     *   1. Insurance (highest discount)
     *   2. Senior citizen (age ≥ 60)
     *   3. No discount (default)
     *
     * OCP: new priority rules or strategies can be added here without touching callers.
     */
    private DiscountStrategy resolveStrategy(BillRequestDTO dto) {
        if (dto.isInsuranceApplicable()) {
            return new InsuranceDiscountStrategy();
        } else if (dto.getPatientAge() != null
                && dto.getPatientAge() >= SeniorCitizenDiscountStrategy.SENIOR_AGE_THRESHOLD) {
            return new SeniorCitizenDiscountStrategy();
        } else {
            return new NoDiscountStrategy();
        }
    }

    // ── Core business methods ──────────────────────────────────────────────

    /**
     * Generates a new bill.
     *
     * STRATEGY: discount is computed by delegating to the resolved {@link DiscountStrategy}.
     * The service only calls {@code strategy.apply(gross)} and reads metadata from the strategy.
     */
    public BillResponseDTO generateBill(BillRequestDTO dto) {
        double gross = dto.getBillItems().stream().mapToDouble(BillItem::getAmount).sum();

        // STRATEGY — select algorithm based on patient eligibility
        DiscountStrategy strategy = resolveStrategy(dto);
        double net         = strategy.apply(gross);
        String discountType = strategy.getDiscountType();
        double discountPct  = strategy.getDiscountPercentage();

        Bill bill = new Bill();
        bill.setPatientId(dto.getPatientId());
        bill.setPatientName(dto.getPatientName());
        bill.setPatientAge(dto.getPatientAge());
        bill.setBillItems(dto.getBillItems());
        bill.setGrossAmount(gross);
        bill.setDiscountType(discountType);
        bill.setDiscountPercentage(discountPct);
        bill.setNetAmount(net);
        bill.setStatus(BillStatus.DRAFT);

        return map(billRepo.save(bill));
    }

    /** Transitions a DRAFT bill to PENDING (issued to the patient). */
    public BillResponseDTO issueBill(Long id) {
        Bill b = getOrThrow(id);
        if (b.getStatus() != BillStatus.DRAFT)
            throw new IllegalStateException("Only DRAFT bills can be issued");
        b.setStatus(BillStatus.PENDING);
        return map(billRepo.save(b));
    }

    /** Processes a payment attempt (PENDING → PAID or PENDING on failure). */
    public BillResponseDTO processPayment(Long id, String method, boolean success) {
        Bill b = getOrThrow(id);
        if (b.getStatus() != BillStatus.PENDING)
            throw new IllegalStateException("Payment only for PENDING bills");
        b.setStatus(BillStatus.PROCESSING);
        b.setPaymentMethod(method);
        billRepo.save(b);
        b.setStatus(success ? BillStatus.PAID : BillStatus.PENDING);
        return map(billRepo.save(b));
    }

    /** Cancels a DRAFT or PENDING bill. */
    public BillResponseDTO cancelBill(Long id) {
        Bill b = getOrThrow(id);
        if (b.getStatus() != BillStatus.DRAFT && b.getStatus() != BillStatus.PENDING)
            throw new IllegalStateException("Only DRAFT/PENDING bills can be cancelled");
        b.setStatus(BillStatus.CANCELLED);
        return map(billRepo.save(b));
    }

    // ── Query methods ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getAllInvoices() {
        return billRepo.findAllByOrderByCreatedAtDesc().stream().map(this::map).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillResponseDTO> getByPatient(Long pid) {
        return billRepo.findByPatientIdOrderByCreatedAtDesc(pid).stream().map(this::map).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BillResponseDTO getById(Long id) { return map(getOrThrow(id)); }

    // ── Private helpers ────────────────────────────────────────────────────

    private Bill getOrThrow(Long id) {
        return billRepo.findById(id).orElseThrow(() -> new RuntimeException("Bill not found: " + id));
    }

    private BillResponseDTO map(Bill b) {
        BillResponseDTO d = new BillResponseDTO();
        d.setBillId(b.getId());
        d.setPatientName(b.getPatientName());
        d.setPatientAge(b.getPatientAge());
        d.setBillItems(b.getBillItems());
        d.setGrossAmount(b.getGrossAmount());
        d.setDiscountType(b.getDiscountType());
        d.setDiscountPercentage(b.getDiscountPercentage());
        d.setNetAmount(b.getNetAmount());
        d.setPaymentMethod(b.getPaymentMethod());
        d.setStatus(b.getStatus());
        d.setCreatedAt(b.getCreatedAt());
        return d;
    }
}
