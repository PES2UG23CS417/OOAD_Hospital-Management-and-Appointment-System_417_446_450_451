package com.hospital.doctor.model;

/**
 * FACTORY METHOD PATTERN — Concrete Product #1 (Doctor Management).
 *
 * Design Pattern: Factory Method (GoF Creational).
 *   - GeneralDoctor is one concrete product created by {@link DoctorFactory}.
 *   - It extends {@link Doctor} and enforces "General Medicine" as its default specialisation.
 *
 *   NOTE on JPA: this class is intentionally NOT an @Entity. The Factory Method pattern
 *   is demonstrated at the Java/OOP level. DoctorFactory creates a GeneralDoctor instance,
 *   sets its fields, and DoctorService persists it via the Doctor base class — keeping the
 *   pattern clean without requiring JPA discriminator column setup.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - General-doctor-specific defaults (specialisation = "General Medicine") live here.
 *
 * GRASP — Information Expert:
 *   - GeneralDoctor knows its own type and default specialisation.
 *
 * GRASP — Creator:
 *   - DoctorFactory (not the controller) is responsible for creating GeneralDoctor instances.
 *
 * GRASP — High Cohesion:
 *   - All fields and methods are directly related to a general practitioner.
 *
 * GRASP — Low Coupling:
 *   - This class is only referenced through the {@link Doctor} base type in the rest of
 *     the system, keeping coupling minimal.
 */
public class GeneralDoctor extends Doctor {

    public GeneralDoctor() {
        super();
        // Enforce default specialisation for GPs
        if (getSpecialization() == null || getSpecialization().isBlank()) {
            setSpecialization(DoctorType.GENERAL.getDefaultSpecialization());
        }
    }

    /** Returns the doctor's type constant for factory use. */
    public DoctorType getDoctorType() { return DoctorType.GENERAL; }

    @Override
    public String toString() {
        return "GeneralDoctor{id=" + getDoctorId() + ", name='" + getName() + "', spec='" + getSpecialization() + "'}";
    }
}
