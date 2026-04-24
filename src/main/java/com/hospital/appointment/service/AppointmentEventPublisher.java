package com.hospital.appointment.service;

import com.hospital.appointment.model.Appointment;

/**
 * OBSERVER PATTERN — Subject interface (Appointment System).
 *
 * Design Pattern: Observer (GoF Behavioural) — also called Publisher-Subscriber.
 *   - Defines the contract for objects that fire appointment events.
 *   - Concrete subjects (AppointmentService) maintain a list of observers and
 *     notify them whenever an appointment is booked, rescheduled, or cancelled.
 *
 * Design Principle: Dependency Inversion Principle (DIP).
 *   - AppointmentService depends on this abstraction (AppointmentEventPublisher),
 *     NOT on concrete listener classes.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - New listeners (email, SMS, audit log) can be added WITHOUT modifying the service.
 */
public interface AppointmentEventPublisher {

    /** Register a new observer. */
    void addObserver(AppointmentObserver observer);

    /** Notify all registered observers that an appointment was booked. */
    void notifyBooked(Appointment appointment);

    /** Notify all registered observers that an appointment was rescheduled. */
    void notifyRescheduled(Appointment appointment);

    /** Notify all registered observers that an appointment was cancelled. */
    void notifyCancelled(Appointment appointment);
}
