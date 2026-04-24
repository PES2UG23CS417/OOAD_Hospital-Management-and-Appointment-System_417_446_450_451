package com.hospital.billing.service;

/**
 * STRATEGY PATTERN — Billing System (GoF Behavioural).
 *
 * Design Pattern: Strategy.
 *   - Defines the contract for all discount-calculation algorithms.
 *   - Concrete strategies: {@link NoDiscountStrategy}, {@link InsuranceDiscountStrategy},
 *     {@link SeniorCitizenDiscountStrategy}.
 *   - {@link BillingService} selects the appropriate strategy at runtime based on
 *     the patient's eligibility, then calls {@link #apply(double)} — it never contains
 *     the discount formula itself.
 *
 * ─── Design Principles ──────────────────────────────────────────────────────
 *
 * • Single Responsibility Principle (SRP):
 *   - Each strategy class has exactly one responsibility: its discount algorithm.
 *   - BillingService does NOT contain any discount calculation logic.
 *
 * • Open/Closed Principle (OCP):
 *   - Adding a new discount type (e.g., StaffDiscount, LoyaltyDiscount) requires
 *     ONLY creating a new strategy class — no changes to BillingService or any
 *     existing strategy.
 *
 * • Dependency Inversion Principle (DIP):
 *   - BillingService depends on this abstraction, not on concrete discount classes.
 *
 * • GRASP — Information Expert:
 *   - Each strategy is the expert on its own discount rate and name.
 */
public interface DiscountStrategy {

    /**
     * Applies the discount algorithm to the given gross amount.
     *
     * @param grossAmount the pre-discount total
     * @return the net (post-discount) amount
     */
    double apply(double grossAmount);

    /** A human-readable label for this discount type (e.g., "INSURANCE", "SENIOR_CITIZEN"). */
    String getDiscountType();

    /** The percentage discount applied, for display on the invoice (0–100). */
    double getDiscountPercentage();
}
