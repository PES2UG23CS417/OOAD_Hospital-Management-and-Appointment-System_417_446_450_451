package com.hospital.doctor.model;

import jakarta.persistence.*;

/**
 * MODEL — Doctor (MVC: Model layer, Doctor Management module).
 *
 * ─── Design Pattern: Factory Method (GoF Creational) ────────────────────────
 *   - Doctor is the BASE PRODUCT in the Factory Method pattern.
 *   - Concrete products: {@link GeneralDoctor} and {@link Specialist} extend this class.
 *   - {@link DoctorFactory} is the Factory that instantiates the correct subtype.
 *
 *   NOTE on JPA: GeneralDoctor and Specialist are plain Java subclasses (not @Entity).
 *   The Factory Method pattern is fully demonstrated at the Java/OOP level — DoctorFactory
 *   creates the correct subtype; DoctorService receives it as a Doctor and persists it.
 *   This avoids JPA discriminator-column issues with pre-existing rows in the DB.
 *
 * ─── GRASP Principles ───────────────────────────────────────────────────────
 *   - Information Expert: Doctor knows its own attributes (name, specialisation,
 *     availability) and encapsulates access through getters/setters.
 *   - Low Coupling: Doctor does not depend on any service or repository class.
 *   - High Cohesion: all fields and methods are directly about a single doctor entity.
 *   - Controller: DoctorController handles HTTP; DoctorService handles business logic —
 *     Doctor itself stays a pure data entity.
 */
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorId;

    private String name;
    private String specialization;
    private boolean availabilityStatus;
    private String email;
    private String phone;

    @Column(name = "doctor_type")
    private String doctorType;

    @Column(name = "dtype")
    private String dtype = "Doctor";

    public Doctor() {}

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int    getDoctorId()             { return doctorId; }
    public void   setDoctorId(int id)       { this.doctorId = id; }

    public String getName()                 { return name; }
    public void   setName(String n)         { this.name = n; }

    public String getSpecialization()       { return specialization; }
    public void   setSpecialization(String s) { this.specialization = s; }

    public boolean isAvailabilityStatus()   { return availabilityStatus; }
    public void    setAvailabilityStatus(boolean a) { this.availabilityStatus = a; }

    public String getEmail()                { return email; }
    public void   setEmail(String e)        { this.email = e; }

    public String getPhone()                { return phone; }
    public void   setPhone(String p)        { this.phone = p; }

    public String getDoctorType()           { return doctorType; }
    public void   setDoctorType(String d)   { this.doctorType = d; }

    public String getDtype()                { return dtype; }
    public void   setDtype(String d)        { this.dtype = d; }
}
