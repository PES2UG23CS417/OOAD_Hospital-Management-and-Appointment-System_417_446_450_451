package com.hospital.patient.service;

import com.hospital.patient.model.Patient;
import java.util.List;

/**
 * SERVICE INTERFACE — Patient Management (MVC: Model layer).
 *
 * ─── Design Principles ──────────────────────────────────────────────────────
 *
 * • Single Responsibility Principle (SRP — SOLID S):
 *   - This interface is solely responsible for defining the contract for patient
 *     business operations. It has one reason to change: if the set of patient
 *     operations changes.
 *
 * • Open/Closed Principle (OCP — SOLID O):
 *   - New patient behaviours can be introduced by creating new implementations
 *     of this interface WITHOUT modifying existing code.
 *   - Example: a CachedPatientService can wrap PatientServiceImpl without changing
 *     the controller or any other class that depends on this interface.
 *
 * • Dependency Inversion Principle (DIP — SOLID D):
 *   - {@link com.hospital.patient.controller.PatientController} and all other
 *     callers depend on THIS ABSTRACTION, never on {@link PatientServiceImpl}
 *     directly. Spring injects the concrete implementation at runtime.
 *
 * ─── GRASP Principles ───────────────────────────────────────────────────────
 *
 * • Controller (GRASP):
 *   - PatientController is the MVC/GRASP Controller that handles HTTP requests
 *     and delegates ALL business decisions to implementations of this interface.
 *
 * • Information Expert (GRASP):
 *   - Implementations of this interface are the Information Experts for patient
 *     lifecycle operations (registration, login, search, update, delete).
 *
 * • Low Coupling (GRASP):
 *   - By depending on this interface rather than PatientServiceImpl, callers
 *     remain loosely coupled to the patient module.
 *
 * • High Cohesion (GRASP):
 *   - All methods declared here relate solely to patient management operations.
 *     No unrelated concerns (appointments, billing) are mixed in.
 */
public interface PatientService {

    /** Registers a new patient and persists the record. */
    Patient register(Patient p);

    /** Returns all patients ordered by registration. */
    List<Patient> getAllPatients();

    /** Full-text search across name, email, phone. */
    List<Patient> search(String query);

    /** Authenticates a patient by email + password. Returns null if credentials are invalid. */
    Patient login(String email, String password);

    /** Updates mutable patient fields (name, phone, age, gender, medical history). */
    Patient updatePatient(int id, Patient updated);

    /** Permanently removes a patient record. */
    void deletePatient(int id);

    /** Looks up a patient by primary key. */
    Patient getById(int id);
}
