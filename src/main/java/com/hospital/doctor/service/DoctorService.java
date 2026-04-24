package com.hospital.doctor.service;

import com.hospital.doctor.model.*;
import com.hospital.doctor.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * SERVICE LAYER — Doctor Management (MVC: Model layer).
 *
 * ─── Design Pattern: Factory Method (GoF Creational) ────────────────────────
 *   - Doctor objects are created exclusively via {@link DoctorFactory}.
 *   - This service never calls {@code new Doctor()}, {@code new GeneralDoctor()},
 *     or {@code new Specialist()} directly — all construction is delegated.
 *
 * ─── GRASP Principles ───────────────────────────────────────────────────────
 *
 * • Controller (GRASP):
 *   - DoctorController handles HTTP requests and delegates ALL business decisions
 *     to this service. This service is the system-level controller for doctor data.
 *
 * • Information Expert:
 *   - This service holds the knowledge of how to manage doctors (create, update,
 *     delete, filter). It is the expert on doctor lifecycle operations.
 *
 * • Low Coupling:
 *   - Depends on {@link DoctorRepository} (interface) and {@link DoctorFactory}
 *     (static utility). No dependency on concrete JPA implementations.
 *   - The controller does not access the repository directly — it goes through here.
 *
 * • High Cohesion:
 *   - Every method in this class is exclusively about managing doctor entities.
 *     No unrelated concerns (billing, appointments) leak into this class.
 *
 * • Creator (GRASP):
 *   - DoctorFactory is the Creator per GRASP; this service delegates to it
 *     rather than creating Doctor objects itself.
 */
@Service
public class DoctorService {

    private final DoctorRepository repo;

    /** DIP: depends on the repository abstraction, not a concrete class. */
    public DoctorService(DoctorRepository repo) { this.repo = repo; }

    /**
     * FACTORY METHOD — creates the correct Doctor subtype (GeneralDoctor or Specialist)
     * based on the {@link DoctorType} supplied by the controller, then persists it.
     *
     * GRASP Creator: DoctorFactory is invoked here so that object creation knowledge
     * is concentrated in one place.
     */
    public Doctor addDoctor(DoctorDTO dto, DoctorType type) {
        // FACTORY — delegate construction; never call new GeneralDoctor() directly here
        Doctor doctor = DoctorFactory.create(type, dto);
        return repo.save(doctor);
    }

    /**
     * Convenience overload used when the form does not send an explicit type
     * (defaults to GENERAL for backward compatibility).
     */
    public Doctor addDoctor(DoctorDTO dto) {
        DoctorType type = (dto.getSpecialization() == null || dto.getSpecialization().isBlank())
                ? DoctorType.GENERAL
                : DoctorType.SPECIALIST;
        return addDoctor(dto, type);
    }

    // ── Query methods ──────────────────────────────────────────────────────

    /** GRASP Information Expert — this service knows how to retrieve all doctors. */
    public List<Doctor> getAllDoctors()                    { return repo.findAll(); }

    /** Returns only available doctors (used by appointment booking). */
    public List<Doctor> getAvailableDoctors()             { return repo.findByAvailabilityStatus(true); }

    /** Filters doctors by specialisation (case-insensitive). */
    public List<Doctor> filterBySpecialization(String sp) { return repo.findBySpecializationIgnoreCase(sp); }

    /** Updates a doctor's availability flag. */
    public void updateAvailability(int id, boolean status) {
        repo.findById(id).ifPresent(d -> { d.setAvailabilityStatus(status); repo.save(d); });
    }

    /** Removes a doctor by ID. */
    public void deleteDoctor(int id) { repo.deleteById(id); }
}
