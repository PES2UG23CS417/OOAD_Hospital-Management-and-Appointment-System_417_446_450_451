package com.hospital.controller;
import com.hospital.patient.service.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    private final PatientService patientService;
    public LoginController(PatientService patientService) { this.patientService = patientService; }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("role") != null) return "redirect:/";
        model.addAttribute("patients", patientService.getAllPatients());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String role,
                          @RequestParam(required = false) Integer patientId,
                          HttpSession session) {
        session.setAttribute("role", role);
        if ("PATIENT".equals(role) && patientId != null) {
            patientService.getAllPatients().stream()
                .filter(p -> p.getId() == patientId)
                .findFirst()
                .ifPresent(p -> {
                    session.setAttribute("patientId", patientId);
                    session.setAttribute("patientName", p.getName());
                });
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
