package com.hospital.controller;
import com.hospital.appointment.repository.AppointmentRepository;
import com.hospital.billing.repository.BillRepository;
import com.hospital.doctor.repository.DoctorRepository;
import com.hospital.patient.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final AppointmentRepository appointmentRepo;
    private final BillRepository billRepo;

    public HomeController(PatientRepository p, DoctorRepository d,
                          AppointmentRepository a, BillRepository b) {
        patientRepo = p; doctorRepo = d; appointmentRepo = a; billRepo = b;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (role == null) return "redirect:/login";
        model.addAttribute("totalPatients", patientRepo.count());
        model.addAttribute("totalDoctors",  doctorRepo.count());
        model.addAttribute("totalAppointments", appointmentRepo.count());
        model.addAttribute("totalBills", billRepo.count());
        return "home";
    }
}
