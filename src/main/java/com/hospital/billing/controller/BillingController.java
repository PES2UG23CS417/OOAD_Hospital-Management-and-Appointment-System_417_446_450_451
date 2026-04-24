package com.hospital.billing.controller;
import com.hospital.billing.dto.*;
import com.hospital.billing.model.BillItem;
import com.hospital.billing.service.BillingService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/billing")
public class BillingController {
    private final BillingService service;
    public BillingController(BillingService service) { this.service = service; }

    private boolean isFinanceAdmin(HttpSession session) {
        return "FINANCE_ADMIN".equals(session.getAttribute("role"));
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!isFinanceAdmin(session)) return "redirect:/";
        List<BillResponseDTO> invoices = service.getAllInvoices();
        model.addAttribute("invoices", invoices);
        model.addAttribute("countDraft",    invoices.stream().filter(i -> "DRAFT".equals(i.getStatus().name())).count());
        model.addAttribute("countPending",  invoices.stream().filter(i -> "PENDING".equals(i.getStatus().name())).count());
        model.addAttribute("countPaid",     invoices.stream().filter(i -> "PAID".equals(i.getStatus().name())).count());
        model.addAttribute("totalRevenue",  invoices.stream().filter(i -> "PAID".equals(i.getStatus().name())).mapToDouble(BillResponseDTO::getNetAmount).sum());
        return "billing/billing-dashboard";
    }

    @GetMapping("/generate")
    public String generateForm(HttpSession session, Model model) {
        if (!isFinanceAdmin(session)) return "redirect:/";
        BillRequestDTO dto = new BillRequestDTO();
        dto.setBillItems(new ArrayList<>(List.of(new BillItem("", 0.0))));
        model.addAttribute("billRequest", dto);
        return "billing/generate-bill";
    }

    @PostMapping("/generate")
    public String generate(@Valid @ModelAttribute("billRequest") BillRequestDTO dto,
                           BindingResult br, HttpSession session, Model model, RedirectAttributes ra) {
        if (!isFinanceAdmin(session)) return "redirect:/";
        if (br.hasErrors()) return "billing/generate-bill";
        try {
            BillResponseDTO b = service.generateBill(dto);
            ra.addFlashAttribute("successMessage", "Bill #" + b.getBillId() + " generated!");
            return "redirect:/billing/invoice/" + b.getBillId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "billing/generate-bill";
        }
    }

    @GetMapping("/invoice/{id}")
    public String invoice(@PathVariable Long id, HttpSession session, Model model) {
        if (!isFinanceAdmin(session)) return "redirect:/";
        model.addAttribute("bill", service.getById(id));
        return "billing/invoice-detail";
    }

    @PostMapping("/issue/{id}")
    public String issue(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isFinanceAdmin(session)) return "redirect:/";
        try { service.issueBill(id); ra.addFlashAttribute("successMessage", "Bill issued."); }
        catch (Exception e) { ra.addFlashAttribute("errorMessage", e.getMessage()); }
        return "redirect:/billing/invoice/" + id;
    }

    @PostMapping("/pay/{id}")
    public String pay(@PathVariable Long id, @RequestParam String paymentMethod,
                      @RequestParam(defaultValue="true") boolean paymentSuccess,
                      HttpSession session, RedirectAttributes ra) {
        if (!isFinanceAdmin(session)) return "redirect:/";
        try {
            BillResponseDTO r = service.processPayment(id, paymentMethod, paymentSuccess);
            ra.addFlashAttribute(r.getStatus().name().equals("PAID") ? "successMessage" : "errorMessage",
                r.getStatus().name().equals("PAID") ? "Payment confirmed!" : "Payment failed. Try again.");
        } catch (Exception e) { ra.addFlashAttribute("errorMessage", e.getMessage()); }
        return "redirect:/billing/invoice/" + id;
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isFinanceAdmin(session)) return "redirect:/";
        try { service.cancelBill(id); ra.addFlashAttribute("successMessage", "Bill cancelled."); }
        catch (Exception e) { ra.addFlashAttribute("errorMessage", e.getMessage()); }
        return "redirect:/billing/history";
    }

    @GetMapping("/history")
    public String history(@RequestParam(required=false) Long patientId,
                          HttpSession session, Model model) {
        if (!isFinanceAdmin(session)) return "redirect:/";
        model.addAttribute("invoices", patientId != null
            ? service.getByPatient(patientId) : service.getAllInvoices());
        model.addAttribute("patientId", patientId);
        return "billing/invoice-history";
    }
}
