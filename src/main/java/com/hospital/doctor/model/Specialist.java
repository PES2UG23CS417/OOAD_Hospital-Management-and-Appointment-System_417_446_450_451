package com.hospital.doctor.model;

/**
 * FACTORY METHOD PATTERN — Concrete Product #2 (Doctor Management).
 *
 * Design Pattern: Factory Method (GoF Creational).
 *   - Specialist is the second concrete product created by {@link DoctorFactory}.
 *   - Unlike GeneralDoctor, a Specialist requires an explicit specialisation (e.g.
 *     "Cardiology", "Neurology") provided at creation time.
 *
 *   NOTE on JPA: this class is intentionally NOT an @Entity. The Factory Method pattern
 *   is demonstrated at the Java/OOP level. DoctorFactory creates a Specialist instance,
 *   sets its fields, and DoctorService persists it via the Doctor base class — keeping the
 *   pattern clean without requiring JPA discriminator column setup.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - Specialist-specific validation (non-blank specialisation) lives here alone.
 *
 * GRASP — Information Expert:
 *   - Specialist knows its own type ({@link DoctorType#SPECIALIST}) and validates
 *     that a specialisation domain is always provided.
 *
 * GRASP — Creator:
 *   - DoctorFactory is the sole creator; no caller constructs Specialist directly.
 *
 * GRASP — High Cohesion:
 *   - All attributes/methods are directly related to a specialist doctor.
 *
 * GRASP — Low Coupling:
 *   - Referenced only through the {@link Doctor} base type in service/controller layers.
 */
public class Specialist extends Doctor {

    public Specialist() {
        super();
    }

    /**
     * Validates that this specialist has a meaningful specialisation.
     * Called by {@link DoctorFactory} immediately after construction.
     */
    public void validate() {
        if (getSpecialization() == null || getSpecialization().isBlank()) {
            throw new IllegalArgumentException(
                    "A Specialist doctor must have a non-blank specialisation (e.g. 'Cardiology').");
        }
    }

    /** Returns the doctor's type constant for factory use. */
    public DoctorType getDoctorType() { return DoctorType.SPECIALIST; }

    @Override
    public String toString() {
        return "Specialist{id=" + getDoctorId() + ", name='" + getName() + "', spec='" + getSpecialization() + "'}";
    }
}
