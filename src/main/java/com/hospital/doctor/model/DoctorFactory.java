package com.hospital.doctor.model;

/**
 * FACTORY METHOD PATTERN — Factory (Doctor Management).
 *
 * Design Pattern: Factory Method (GoF Creational).
 *   - This is the FACTORY class. It encapsulates the instantiation logic for all
 *     Doctor subtypes ({@link GeneralDoctor}, {@link Specialist}).
 *   - Callers (DoctorService) only know about the {@link Doctor} base type;
 *     they never call {@code new GeneralDoctor()} or {@code new Specialist()} directly.
 *
 *   HOW THE PATTERN WORKS HERE:
 *   1. The factory instantiates the correct Java subtype (GeneralDoctor / Specialist).
 *   2. The subtype applies its own type-specific logic:
 *      - GeneralDoctor: sets the default "General Medicine" specialisation.
 *      - Specialist: validates that a non-blank specialisation is provided.
 *   3. The factory then maps the subtype's state into a plain {@link Doctor} @Entity
 *      for JPA persistence.  GeneralDoctor / Specialist are intentionally NOT @Entity
 *      to avoid JPA discriminator-column conflicts with pre-existing data.
 *
 * ─── Design Principles ──────────────────────────────────────────────────────
 *
 * • Single Responsibility Principle (SRP):
 *   - Doctor creation is the ONLY responsibility of this class.
 *
 * • Open/Closed Principle (OCP):
 *   - Adding a new doctor type only requires:
 *       (a) a new constant in {@link DoctorType}
 *       (b) a new plain-Java subclass extending Doctor
 *       (c) a new case in this factory's switch
 *     — zero changes to DoctorService, DoctorController, or the repository.
 *
 * ─── GRASP Principles ───────────────────────────────────────────────────────
 *
 * • Creator: DoctorFactory satisfies GRASP Creator — it has all the initialising
 *   data (DoctorDTO, DoctorType) needed to construct Doctor objects.
 *
 * • Information Expert: the factory knows which subtype to instantiate for a given
 *   {@link DoctorType} — that knowledge belongs here, not in the service.
 *
 * • Low Coupling: DoctorService depends only on {@link Doctor} and DoctorFactory;
 *   it has no direct dependency on GeneralDoctor or Specialist.
 *
 * • High Cohesion: every method in this class is about constructing Doctor objects.
 */
public class DoctorFactory {

    private DoctorFactory() { /* utility class — never instantiated */ }

    /**
     * Creates and returns a fully initialised {@link Doctor} entity based on the
     * given {@link DoctorType} and data from {@link DoctorDTO}.
     *
     * <p>Internally instantiates the appropriate Java subtype to apply its
     * type-specific defaults and validations, then copies the resulting state
     * into a plain {@link Doctor} @Entity for safe JPA persistence.</p>
     *
     * @param type the desired doctor type (GENERAL or SPECIALIST)
     * @param dto  the data-transfer object carrying form values
     * @return a plain {@link Doctor} entity ready to be saved by the repository
     * @throws IllegalArgumentException if type is SPECIALIST but specialisation is blank
     */
    public static Doctor create(DoctorType type, DoctorDTO dto) {

        // ── Step 1: Create the correct Java subtype for type-specific logic ──
        Doctor typed;

        switch (type) {
            case GENERAL -> {
                // FACTORY — create GeneralDoctor to apply GP-specific defaulting
                GeneralDoctor gp = new GeneralDoctor();
                // GeneralDoctor constructor already defaults specialisation to "General Medicine";
                // only override if the user explicitly provided one.
                if (dto.getSpecialization() != null && !dto.getSpecialization().isBlank()) {
                    gp.setSpecialization(dto.getSpecialization());
                }
                typed = gp;
            }
            case SPECIALIST -> {
                // FACTORY — create Specialist to apply specialist-specific validation
                Specialist sp = new Specialist();
                sp.setSpecialization(dto.getSpecialization());
                sp.validate();   // throws IllegalArgumentException if specialisation is blank
                typed = sp;
            }
            default -> throw new IllegalArgumentException("Unknown DoctorType: " + type);
        }

        // Apply common attributes on the subtype
        typed.setName(dto.getName());
        typed.setEmail(dto.getEmail());
        typed.setPhone(dto.getPhone());
        typed.setAvailabilityStatus(dto.isAvailabilityStatus());

        // ── Step 2: Map subtype state into a plain Doctor @Entity for JPA ────
        // GeneralDoctor / Specialist are Java-level products (not @Entity).
        // Copying fields here keeps JPA happy while preserving the OOP pattern.
        Doctor entity = new Doctor();
        entity.setName(typed.getName());
        entity.setSpecialization(typed.getSpecialization());
        entity.setEmail(typed.getEmail());
        entity.setPhone(typed.getPhone());
        entity.setAvailabilityStatus(typed.isAvailabilityStatus());
        entity.setDoctorType(type.name());

        return entity;
    }
}
