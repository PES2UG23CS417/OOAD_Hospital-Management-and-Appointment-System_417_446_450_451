package com.hospital.patient.controller;

import com.hospital.patient.model.Patient;
import com.hospital.patient.service.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;
    public PatientController(PatientService service) { this.service = service; }

    @GetMapping
    public String dashboard(@RequestParam(required = false) String search,
                            @RequestParam(required = false) String tab,
                            HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (!"HOSPITAL_STAFF".equals(role)) return "redirect:/";

        var patients = (search != null && !search.isBlank())
            ? service.search(search) : service.getAllPatients();

        model.addAttribute("patients",   patients);
        model.addAttribute("search",     search);
        model.addAttribute("activeTab",  tab != null ? tab : "register");

        /*
         * FIX: The patients/dashboard.html template (adapted from V2) references
         * ${cacheSize} and ${totalRegistered} which came from the Singleton
         * PatientRegistry in V2. V1 has no such registry, so these attributes
         * were never put in the model -> Thymeleaf EvaluationException -> HTTP 500.
         *
         * Fix: Provide fallback values so the template renders cleanly.
         * The template will show total patient count as both values, which is
         * accurate for V1 (no cache — every lookup goes to the DB).
         */
        long totalPatients = service.getAllPatients().size();
        model.addAttribute("cacheSize",       totalPatients);   // V1: all patients are "in DB"
        model.addAttribute("totalRegistered", totalPatients);

        return "patients/dashboard";
    }

    @PostMapping("/add")
    public String addPatient(@RequestParam String name,  @RequestParam String email,
                              @RequestParam String password, @RequestParam String phone,
                              @RequestParam int age,    @RequestParam String gender,
                              RedirectAttributes ra) {
        try {
            service.register(new Patient(name, email, password, phone, age, gender));
            ra.addFlashAttribute("flashMsg",  "Patient registered successfully!");
            ra.addFlashAttribute("flashType", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("flashMsg",  "Registration failed: " + e.getMessage());
            ra.addFlashAttribute("flashType", "error");
        }
        return "redirect:/patients?tab=view";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes ra) {
        service.deletePatient(id);
        ra.addFlashAttribute("flashMsg",  "Patient record removed.");
        ra.addFlashAttribute("flashType", "success");
        return "redirect:/patients?tab=view";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable int id,
                          @RequestParam String name,   @RequestParam String phone,
                          @RequestParam int    age,    @RequestParam String gender,
                          @RequestParam(required = false) String medicalHistory,
                          RedirectAttributes ra) {
        Patient updated = new Patient();
        updated.setName(name);   updated.setPhone(phone);
        updated.setAge(age);     updated.setGender(gender);
        updated.setMedicalHistory(medicalHistory);
        service.updatePatient(id, updated);
        ra.addFlashAttribute("flashMsg",  "Patient updated successfully.");
        ra.addFlashAttribute("flashType", "success");
        return "redirect:/patients?tab=view";
    }
}
