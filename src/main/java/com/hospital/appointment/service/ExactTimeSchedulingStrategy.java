package com.hospital.appointment.service;

import com.hospital.appointment.model.AppointmentSlot;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

/**
 * STRATEGY PATTERN — Concrete Strategy #1 (Appointment System).
 *
 * Design Pattern: Strategy (GoF Behavioural).
 *   - Implements {@link AppointmentSchedulingStrategy} to book the EXACT requested slot.
 *   - Used when the patient specifies a precise date/time (standard booking flow).
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - This class has one and only one reason to change: the exact-time matching algorithm.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - Adding a new strategy (e.g. "nearest available") requires NO change to this class.
 */
@Component("exactTimeStrategy")
public class ExactTimeSchedulingStrategy implements AppointmentSchedulingStrategy {

    @Override
    public AppointmentSlot selectSlot(LocalDateTime requestedTime, List<AppointmentSlot> availableSlots) {
        if (requestedTime == null) return null;
        // GRASP — Information Expert: the strategy owns the slot-selection logic.
        return availableSlots.stream()
                .filter(s -> s.getSlotTime().equals(requestedTime))
                .findFirst()
                .orElse(null);
    }
}
