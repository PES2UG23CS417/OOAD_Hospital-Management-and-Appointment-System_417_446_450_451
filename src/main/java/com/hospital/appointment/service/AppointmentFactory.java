package com.hospital.appointment.service;

import com.hospital.appointment.model.Appointment;
import com.hospital.appointment.model.AppointmentSlot;
import com.hospital.appointment.model.AppointmentStatus;

/**
 * FACTORY PATTERN (GoF Creational) — Appointment System.
 *
 * Design Pattern: Factory Method / Static Factory.
 *   - Centralises the creation of {@link Appointment} objects.
 *   - The controller and service NEVER call {@code new Appointment()} directly;
 *     all creation flows through this factory, ensuring consistent initialisation.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - Object creation is the sole responsibility of this class.
 *   - AppointmentService is relieved from knowing how to construct appointments.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - Adding a new kind of appointment (e.g., TelemedicineAppointment) only requires
 *     adding a new factory method here — no changes to existing callers.
 *
 * GRASP — Creator: this class satisfies the Creator pattern (it aggregates/creates Appointment).
 */
public class AppointmentFactory {

    private AppointmentFactory() { /* utility — not instantiated */ }

    /**
     * Creates a standard (in-person) scheduled appointment from the given slot and patient data.
     *
     * @param patientId   the patient's ID
     * @param patientName the patient's display name
     * @param slot        the slot that was reserved (must already be saved as booked)
     * @param notes       optional notes provided by the patient
     * @return a fully initialised, un-persisted {@link Appointment} ready to be saved
     */
    public static Appointment createScheduledAppointment(Long patientId, String patientName,
                                                          AppointmentSlot slot, String notes) {
        Appointment a = new Appointment();
        a.setPatientId(patientId);
        a.setPatientName(patientName);
        a.setDoctorId(slot.getDoctorId());
        a.setDoctorName(slot.getDoctorName());
        a.setAppointmentTime(slot.getSlotTime());
        a.setNotes(notes);
        a.setStatus(AppointmentStatus.SCHEDULED);
        return a;
    }

    /**
     * Creates a rescheduled appointment (same patient/doctor, new slot).
     * The existing appointment entity is mutated so its ID is preserved in the DB.
     *
     * @param existing the appointment to reschedule
     * @param newSlot  the new slot (must already be saved as booked)
     * @return the mutated appointment entity ready to be saved
     */
    public static Appointment createRescheduledAppointment(Appointment existing, AppointmentSlot newSlot) {
        existing.setAppointmentTime(newSlot.getSlotTime());
        existing.setStatus(AppointmentStatus.RESCHEDULED);
        return existing;
    }
}
