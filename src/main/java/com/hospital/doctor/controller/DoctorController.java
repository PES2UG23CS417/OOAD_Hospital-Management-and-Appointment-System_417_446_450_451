package com.hospital.doctor.controller;

import com.hospital.doctor.model.DoctorDTO;
import com.hospital.doctor.model.DoctorType;
import com.hospital.doctor.service.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CONTROLLER — Doctor Management (MVC: Controller layer).
 *
 * ─── MVC Role ───────────────────────────────────────────────────────────────
 *   - Receives HTTP requests, performs minimal validation, and delegates ALL
 *     business decisions to {@link DoctorService}.
 *   - Populates the Spring {@link Model} for Thymeleaf views.
 *   - Never contains business logic or discount calculations.
 *
 * ─── GRASP — Controller ─────────────────────────────────────────────────────
 *   - This class is the GRASP Controller for the Doctor Management module.
 *     It handles system events (HTTP POST/GET) and routes them to the service layer.
 *
 * ─── GRASP — Low Coupling ───────────────────────────────────────────────────
 *   - Depends only on DoctorService (interface-backed). Has no knowledge of
 *     DoctorFactory, GeneralDoctor, or Specialist — those are implementation details.
 *
 * ─── Design Pattern: Factory Method ─────────────────────────────────────────
 *   - The {@code doctorType} request parameter is parsed to a {@link DoctorType}
 *     enum and forwarded to {@link DoctorService#addDoctor(DoctorDTO, DoctorType)}.
 *   - DoctorService → DoctorFactory then creates the correct subtype
 *     (GeneralDoctor or Specialist) transparently.
 */
@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService service;

    /** DIP: controller depends on the service abstraction, not a concrete class. */
    public DoctorController(DoctorService service) { this.service = service; }

    @GetMapping
    public String list(@RequestParam(required = false) String specialization,
                       HttpSession session, Model model) {
        if (!"HOSPITAL_STAFF".equals(session.getAttribute("role"))) return "redirect:/";
        model.addAttribute("doctors",
                specialization != null && !specialization.isBlank()
                        ? service.filterBySpecialization(specialization)
                        : service.getAllDoctors());
        model.addAttribute("filterSpec", specialization);
        return "doctors/list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        if (!"HOSPITAL_STAFF".equals(session.getAttribute("role"))) return "redirect:/";
        model.addAttribute("doctorDTO",    new DoctorDTO());
        model.addAttribute("selectedType", "GENERAL");
        model.addAttribute("doctorTypes",  DoctorType.values());   // for the form dropdown
        return "doctors/add";
    }

    /**
     * FACTORY METHOD — the {@code doctorType} param (GENERAL or SPECIALIST) is passed
     * to {@link DoctorService#addDoctor(DoctorDTO, DoctorType)} which delegates to
     * {@link com.hospital.doctor.model.DoctorFactory} to create the correct subtype.
     */
    @PostMapping("/add")
    public String addDoctor(@ModelAttribute DoctorDTO dto,
                            @RequestParam(defaultValue = "GENERAL") String doctorType,
                            HttpSession session, RedirectAttributes ra) {
        if (!"HOSPITAL_STAFF".equals(session.getAttribute("role"))) return "redirect:/";
        try {
            // Parse the form string to the DoctorType enum; default to GENERAL on bad input
            DoctorType type;
            try {
                type = DoctorType.valueOf(doctorType.toUpperCase());
            } catch (IllegalArgumentException ex) {
                type = DoctorType.GENERAL;
            }
            // FACTORY — service internally calls DoctorFactory.create(type, dto)
            service.addDoctor(dto, type);
            ra.addFlashAttribute("flashMsg",  "Doctor added successfully!");
            ra.addFlashAttribute("flashType", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/doctors/add";
        }
        return "redirect:/doctors";
    }

    @PostMapping("/availability/{id}")
    public String updateAvailability(@PathVariable int id, @RequestParam boolean status,
                                     HttpSession session, RedirectAttributes ra) {
        if (!"HOSPITAL_STAFF".equals(session.getAttribute("role"))) return "redirect:/";
        service.updateAvailability(id, status);
        ra.addFlashAttribute("flashMsg",  "Availability updated.");
        ra.addFlashAttribute("flashType", "success");
        return "redirect:/doctors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes ra) {
        if (!"HOSPITAL_STAFF".equals(session.getAttribute("role"))) return "redirect:/";
        service.deleteDoctor(id);
        ra.addFlashAttribute("flashMsg",  "Doctor removed.");
        ra.addFlashAttribute("flashType", "success");
        return "redirect:/doctors";
    }
}
