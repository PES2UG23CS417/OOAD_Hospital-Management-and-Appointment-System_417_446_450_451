package com.hospital.appointment.service;

import com.hospital.appointment.model.Appointment;
import com.hospital.appointment.model.AppointmentSlot;

/**
 * STRATEGY PATTERN (GoF Behavioural) — Appointment System.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - The AppointmentService is OPEN for extension (new scheduling strategies can be added)
 *     without MODIFYING the existing service code.
 *
 * Design Principle: Dependency Inversion Principle (DIP).
 *   - AppointmentService depends on this abstraction, not on concrete scheduling algorithms.
 *
 * This interface defines the contract for scheduling an appointment into a slot.
 * Concrete strategies (ImmediateSchedulingStrategy, PreferredTimeSchedulingStrategy)
 * implement different booking algorithms.
 */
public interface AppointmentSchedulingStrategy {

    /**
     * GRASP — Information Expert: the strategy knows how to select the best slot.
     * SRP: each strategy class has exactly one reason to change (its algorithm).
     *
     * @param requestedTime   the patient's preferred slot time (may be null for "any")
     * @param availableSlots  all free slots for the target doctor
     * @return the slot chosen by this strategy, or {@code null} if none is suitable
     */
    AppointmentSlot selectSlot(java.time.LocalDateTime requestedTime,
                               java.util.List<AppointmentSlot> availableSlots);
}
