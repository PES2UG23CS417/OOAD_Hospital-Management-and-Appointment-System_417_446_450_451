package com.hospital.appointment.service;

import com.hospital.appointment.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * OBSERVER PATTERN — Concrete Observer #2 (Appointment System).
 *
 * Design Pattern: Observer (GoF Behavioural).
 *   - Reacts to appointment lifecycle events by simulating patient notifications
 *     (e.g., SMS/email). In production this would call a real notification service.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - This class has exactly one reason to change: the patient notification requirement.
 *   - Audit concerns are isolated in {@link AppointmentAuditObserver}.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - New notification channels (push, WhatsApp) can be added here without affecting
 *     AppointmentService or any other observer.
 */
@Component
public class AppointmentNotificationObserver implements AppointmentObserver {

    private static final Logger log = LoggerFactory.getLogger(AppointmentNotificationObserver.class);

    @Override
    public void onBooked(Appointment appointment) {
        // In production: call email/SMS gateway here
        log.info("[NOTIFY] Sending booking confirmation to patient '{}' for appointment at {}",
                appointment.getPatientName(), appointment.getAppointmentTime());
    }

    @Override
    public void onRescheduled(Appointment appointment) {
        log.info("[NOTIFY] Sending reschedule notification to patient '{}' — new time: {}",
                appointment.getPatientName(), appointment.getAppointmentTime());
    }

    @Override
    public void onCancelled(Appointment appointment) {
        log.info("[NOTIFY] Sending cancellation notice to patient '{}'",
                appointment.getPatientName());
    }
}
