package com.hospital.appointment.controller;

import com.hospital.appointment.model.*;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.doctor.service.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public AppointmentController(AppointmentService a, DoctorService d) {
        appointmentService = a; doctorService = d;
    }

    @GetMapping
    public String appointments(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if ("PATIENT".equals(role))        return "redirect:/appointments/patient-dashboard";
        if ("HOSPITAL_STAFF".equals(role)) return "redirect:/appointments/staff-dashboard";
        return "redirect:/";
    }

    @GetMapping("/patient-dashboard")
    public String patientDashboard(HttpSession session, Model model) {
        if (!"PATIENT".equals(session.getAttribute("role"))) return "redirect:/";
        /*
         * FIX: The session stores patientId as Integer (set from @RequestParam Integer patientId
         * in LoginController). Safely extract and convert to Long without NPE.
         * Using Number.longValue() handles both Integer and Long transparently.
         */
        Object pidObj = session.getAttribute("patientId");
        if (pidObj == null) return "redirect:/";
        Long pid = ((Number) pidObj).longValue();
        model.addAttribute("appointments", appointmentService.getByPatient(pid));
        return "appointments/patient_dashboard";
    }

    @GetMapping("/staff-dashboard")
    public String staffDashboard(HttpSession session, Model model) {
        if (!"HOSPITAL_STAFF".equals(session.getAttribute("role"))) return "redirect:/";
        model.addAttribute("appointments",   appointmentService.getAll());
        model.addAttribute("availableSlots", appointmentService.getAvailableSlots());
        model.addAttribute("doctors",        doctorService.getAllDoctors());
        return "appointments/staff_dashboard";
    }

    @GetMapping("/book")
    public String bookForm(HttpSession session, Model model) {
        if (!"PATIENT".equals(session.getAttribute("role"))) return "redirect:/";
        model.addAttribute("allSlots", appointmentService.getAvailableSlots());
        model.addAttribute("doctors",  doctorService.getAvailableDoctors());
        return "appointments/book_appointment";
    }

    @PostMapping("/book")
    public String book(@RequestParam Integer doctorId,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentTime,
                       @RequestParam(required = false) String notes,
                       HttpSession session, RedirectAttributes ra) {
        if (!"PATIENT".equals(session.getAttribute("role"))) return "redirect:/";
        try {
            // FIX: same safe cast using Number
            Object pidObj = session.getAttribute("patientId");
            if (pidObj == null) return "redirect:/";
            Long pid  = ((Number) pidObj).longValue();
            String pn = (String) session.getAttribute("patientName");
            appointmentService.bookAppointment(pid, pn, doctorId, appointmentTime, notes);
            ra.addFlashAttribute("successMsg", "Appointment booked successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Booking failed: " + e.getMessage());
        }
        return "redirect:/appointments/patient-dashboard";
    }

    @GetMapping("/reschedule/{id}")
    public String rescheduleForm(@PathVariable Long id, HttpSession session, Model model) {
        Appointment a = appointmentService.getById(id);
        if (a == null) return "redirect:/appointments";
        model.addAttribute("appointment", a);
        model.addAttribute("doctorSlots", appointmentService.getAvailableSlotsByDoctor(a.getDoctorId()));
        return "appointments/reschedule_appointment";
    }

    @PostMapping("/reschedule/{id}")
    public String reschedule(@PathVariable Long id,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newTime,
                             RedirectAttributes ra) {
        try {
            appointmentService.reschedule(id, newTime);
            ra.addFlashAttribute("successMsg", "Appointment rescheduled!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Reschedule failed: " + e.getMessage());
        }
        return "redirect:/appointments/patient-dashboard";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        try {
            appointmentService.cancel(id);
            ra.addFlashAttribute("successMsg", "Appointment cancelled.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        String role = (String) session.getAttribute("role");
        return "PATIENT".equals(role)
            ? "redirect:/appointments/patient-dashboard"
            : "redirect:/appointments/staff-dashboard";
    }

    @PostMapping("/add-slot")
    public String addSlot(@RequestParam Integer doctorId,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime slotTime,
                          HttpSession session, RedirectAttributes ra) {
        if (!"HOSPITAL_STAFF".equals(session.getAttribute("role"))) return "redirect:/";
        try {
            appointmentService.addSlot(doctorId, slotTime);
            ra.addFlashAttribute("successMsg", "Slot added successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/appointments/staff-dashboard";
    }
}
