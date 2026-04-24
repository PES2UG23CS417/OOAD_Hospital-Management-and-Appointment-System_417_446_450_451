package com.hospital.appointment.service;

import com.hospital.appointment.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * OBSERVER PATTERN — Concrete Observer #1 (Appointment System).
 *
 * Design Pattern: Observer (GoF Behavioural).
 *   - Reacts to appointment lifecycle events by writing an audit trail to the log.
 *   - Registered automatically with AppointmentService via Spring DI.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - This class has exactly one reason to change: the audit-logging requirement.
 *   - Notification concerns live in {@link AppointmentNotificationObserver}.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - New event types can be added to {@link AppointmentObserver} without touching this class.
 */
@Component
public class AppointmentAuditObserver implements AppointmentObserver {

    private static final Logger log = LoggerFactory.getLogger(AppointmentAuditObserver.class);

    @Override
    public void onBooked(Appointment appointment) {
        log.info("[AUDIT] Appointment BOOKED — id={}, patient='{}', doctor='{}', time={}",
                appointment.getId(), appointment.getPatientName(),
                appointment.getDoctorName(), appointment.getAppointmentTime());
    }

    @Override
    public void onRescheduled(Appointment appointment) {
        log.info("[AUDIT] Appointment RESCHEDULED — id={}, new time={}",
                appointment.getId(), appointment.getAppointmentTime());
    }

    @Override
    public void onCancelled(Appointment appointment) {
        log.info("[AUDIT] Appointment CANCELLED — id={}, patient='{}'",
                appointment.getId(), appointment.getPatientName());
    }
}
