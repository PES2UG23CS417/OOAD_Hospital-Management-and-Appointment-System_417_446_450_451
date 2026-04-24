package com.hospital.appointment.service;

import com.hospital.appointment.model.AppointmentSlot;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

/**
 * STRATEGY PATTERN — Concrete Strategy #2 (Appointment System).
 *
 * Design Pattern: Strategy (GoF Behavioural).
 *   - Implements {@link AppointmentSchedulingStrategy} to pick the earliest available slot
 *     when no specific time is requested (walk-in or next-available booking).
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - One reason to change: the "earliest available" selection algorithm.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - AppointmentService selects a strategy at runtime; this strategy is fully
 *     independent and does not need to modify any existing code to be used.
 */
@Component("nextAvailableStrategy")
public class NextAvailableSchedulingStrategy implements AppointmentSchedulingStrategy {

    @Override
    public AppointmentSlot selectSlot(LocalDateTime requestedTime, List<AppointmentSlot> availableSlots) {
        // GRASP — Information Expert: this strategy knows best how to find the earliest slot.
        return availableSlots.stream()
                .filter(s -> !s.isBooked())
                .min(java.util.Comparator.comparing(AppointmentSlot::getSlotTime))
                .orElse(null);
    }
}
