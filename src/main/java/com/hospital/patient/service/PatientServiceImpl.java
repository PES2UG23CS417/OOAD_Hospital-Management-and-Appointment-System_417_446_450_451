package com.hospital.patient.service;

import com.hospital.patient.model.Patient;
import com.hospital.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * SERVICE IMPLEMENTATION — Patient Management (MVC: Model layer).
 *
 * ─── Design Principles (SOLID) ──────────────────────────────────────────────
 *
 * • Single Responsibility Principle (SRP — SOLID S):
 *   - This class has exactly ONE responsibility: implementing patient business logic.
 *   - HTTP handling → PatientController.
 *   - Database access → PatientRepository.
 *   - This class only orchestrates between those two layers.
 *
 * • Open/Closed Principle (OCP — SOLID O):
 *   - PatientController is programmed to {@link PatientService} (the interface).
 *   - A new implementation (e.g., CachedPatientServiceImpl) can be substituted
 *     without changing the controller or any test — this class is CLOSED for modification.
 *
 * • Dependency Inversion Principle (DIP — SOLID D):
 *   - This class depends on {@link PatientRepository} (a Spring Data INTERFACE),
 *     never on a concrete repository class.
 *   - Spring injects the correct implementation at runtime.
 *
 * ─── GRASP Principles ───────────────────────────────────────────────────────
 *
 * • Controller (GRASP):
 *   - PatientController is the GRASP Controller for the Patient module.
 *     It receives HTTP requests and delegates ALL business decisions to this class.
 *   - This class acts as the second-level system controller (service layer).
 *
 * • Information Expert (GRASP):
 *   - This service implementation is the Information Expert for patient operations.
 *     It holds all the logic for registering, authenticating, updating, and deleting
 *     patients — knowledge that is cohesively grouped here.
 *
 * • Low Coupling (GRASP):
 *   - The controller injects PatientService (interface), not PatientServiceImpl.
 *   - This implementation only couples to PatientRepository — one dependency.
 *   - No direct dependency on any other module (billing, appointments, doctors).
 *
 * • High Cohesion (GRASP):
 *   - Every method in this class is exclusively about managing Patient entities.
 *   - No billing logic, no appointment logic, no doctor management leaks in.
 */
@Service
public class PatientServiceImpl implements PatientService {

    /** DIP: depend on the repository abstraction injected by Spring. */
    private final PatientRepository repo;

    public PatientServiceImpl(PatientRepository repo) { this.repo = repo; }

    // ── PatientService operations ──────────────────────────────────────────

    /**
     * SRP: registration is the only thing this method does.
     * Information Expert: this service knows how to persist a patient.
     */
    @Override
    public Patient register(Patient p) { return repo.save(p); }

    /**
     * Information Expert: the service is the expert on how to retrieve all patients.
     */
    @Override
    public List<Patient> getAllPatients() { return repo.findAll(); }

    /**
     * Delegates full-text search to the repository query (Information Expert).
     */
    @Override
    public List<Patient> search(String query) { return repo.searchPatients(query); }

    /**
     * SRP: authentication logic (email + password check) is isolated here.
     * Low Coupling: no dependency on session, HTTP, or external auth libraries.
     */
    @Override
    public Patient login(String email, String password) {
        return repo.findByEmail(email)
                   .filter(p -> p.getPassword().equals(password))
                   .orElse(null);
    }

    /**
     * SRP: update logic (which fields are mutable) is exclusively here.
     * OCP: if new fields are added to Patient, only this method needs updating.
     */
    @Override
    public Patient updatePatient(int id, Patient updated) {
        Patient p = repo.findById(id).orElse(null);
        if (p == null) return null;
        p.setName(updated.getName());
        p.setPhone(updated.getPhone());
        p.setAge(updated.getAge());
        p.setGender(updated.getGender());
        if (updated.getMedicalHistory() != null) p.setMedicalHistory(updated.getMedicalHistory());
        return repo.save(p);
    }

    @Override
    public void deletePatient(int id) { repo.deleteById(id); }

    @Override
    public Patient getById(int id) { return repo.findById(id).orElse(null); }
}
