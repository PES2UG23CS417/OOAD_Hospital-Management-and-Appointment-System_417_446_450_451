package com.hospital.doctor.model;

/**
 * FACTORY METHOD PATTERN — Doctor Management (GoF Creational).
 *
 * Design Pattern: Factory Method.
 *   - {@link DoctorFactory} uses this enum to decide which concrete Doctor
 *     subtype to instantiate (GeneralDoctor vs Specialist).
 *
 * Design Principle: Open/Closed Principle (OCP) — GRASP Creator.
 *   - Adding a new doctor type only requires adding a new enum constant and
 *     a corresponding branch in DoctorFactory; no existing code changes.
 *
 * GRASP — Information Expert:
 *   - DoctorType knows its own display label and default specialisation.
 */
public enum DoctorType {

    /** A general practitioner who handles routine / non-specialist cases. */
    GENERAL("General Practitioner", "General Medicine"),

    /** A specialist with a specific domain of medicine. */
    SPECIALIST("Specialist", null);   // specialisation is provided externally for specialists

    private final String displayName;
    private final String defaultSpecialization;

    DoctorType(String displayName, String defaultSpecialization) {
        this.displayName           = displayName;
        this.defaultSpecialization = defaultSpecialization;
    }

    public String getDisplayName()          { return displayName; }
    public String getDefaultSpecialization() { return defaultSpecialization; }
}
