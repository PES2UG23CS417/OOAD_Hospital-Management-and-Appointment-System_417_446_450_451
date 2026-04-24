package com.hospital.appointment.service;

import com.hospital.appointment.model.Appointment;

/**
 * OBSERVER PATTERN — Observer interface (Appointment System).
 *
 * Design Pattern: Observer (GoF Behavioural).
 *   - Any class that wants to react to appointment lifecycle events implements this interface.
 *   - Concrete observers: {@link AppointmentAuditObserver}, {@link AppointmentNotificationObserver}.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - Each concrete observer has ONE reason to change (audit logic vs notification logic).
 *
 * Design Principle: Dependency Inversion Principle (DIP).
 *   - AppointmentService depends on THIS abstraction, never on concrete observer classes.
 */
public interface AppointmentObserver {

    /** Called when a new appointment has been booked. */
    void onBooked(Appointment appointment);

    /** Called when an appointment has been rescheduled. */
    void onRescheduled(Appointment appointment);

    /** Called when an appointment has been cancelled. */
    void onCancelled(Appointment appointment);
}
