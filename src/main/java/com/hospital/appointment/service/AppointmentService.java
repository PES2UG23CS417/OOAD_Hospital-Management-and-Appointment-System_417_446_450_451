package com.hospital.appointment.service;

import com.hospital.appointment.model.*;
import com.hospital.appointment.repository.*;
import com.hospital.doctor.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE LAYER — Appointment System (MVC: Model layer).
 *
 * ─── Design Patterns ────────────────────────────────────────────────────────
 *
 * 1. FACTORY (GoF Creational):
 *    {@link AppointmentFactory} centralises all Appointment object creation.
 *    This service never calls {@code new Appointment()} directly.
 *
 * 2. STRATEGY (GoF Behavioural):
 *    Slot selection is delegated to an {@link AppointmentSchedulingStrategy}.
 *    The default strategy is {@link ExactTimeSchedulingStrategy} (exact-time match).
 *    The strategy can be swapped at runtime (e.g., for next-available booking).
 *
 * 3. OBSERVER (GoF Behavioural) via {@link AppointmentEventPublisher}:
 *    After every state change this service fires an event so that observers
 *    (audit logger, notification sender) react independently.
 *
 * ─── Design Principles ──────────────────────────────────────────────────────
 *
 * • Single Responsibility Principle (SRP):
 *   - Appointment creation → AppointmentFactory
 *   - Slot selection algorithm → AppointmentSchedulingStrategy implementations
 *   - Audit / notification → AppointmentObserver implementations
 *   - This service only orchestrates; it does NOT do the work of those classes.
 *
 * • Open/Closed Principle (OCP):
 *   - New scheduling algorithms → add a new Strategy; no change here.
 *   - New event reactions → add a new Observer; no change here.
 *
 * • Dependency Inversion Principle (DIP):
 *   - Depends on repository interfaces (Spring Data), strategy interface, and
 *     observer interface — never on concrete implementations.
 *
 * ─── GRASP Principles ───────────────────────────────────────────────────────
 *
 * • Controller: AppointmentController delegates all business decisions here.
 * • Information Expert: this service holds the knowledge to coordinate booking.
 * • Low Coupling: strategy and observer are injected; no hard dependencies.
 * • High Cohesion: all methods relate solely to appointment lifecycle management.
 */
@Service
public class AppointmentService implements AppointmentEventPublisher {

    // ── Repositories ──────────────────────────────────────────────────────
    private final AppointmentRepository appointmentRepo;
    private final AppointmentSlotRepository slotRepo;
    private final DoctorRepository doctorRepo;

    // ── Strategy (injected, swappable at runtime) ─────────────────────────
    private AppointmentSchedulingStrategy schedulingStrategy;

    // ── Observer list (OBSERVER pattern — subject maintains observers) ─────
    private final List<AppointmentObserver> observers = new ArrayList<>();

    /**
     * Constructor — DIP: all dependencies are abstractions, injected by Spring.
     * Observers are injected as a list of all {@link AppointmentObserver} beans.
     * The default strategy is {@link ExactTimeSchedulingStrategy}.
     */
    public AppointmentService(AppointmentRepository appointmentRepo,
                               AppointmentSlotRepository slotRepo,
                               DoctorRepository doctorRepo,
                               ExactTimeSchedulingStrategy defaultStrategy,
                               List<AppointmentObserver> observerBeans) {
        this.appointmentRepo      = appointmentRepo;
        this.slotRepo             = slotRepo;
        this.doctorRepo           = doctorRepo;
        this.schedulingStrategy   = defaultStrategy;          // default Strategy
        this.observers.addAll(observerBeans);                 // register all Observers
    }

    // ── AppointmentEventPublisher (OBSERVER subject) ───────────────────────

    @Override
    public void addObserver(AppointmentObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyBooked(Appointment appointment) {
        observers.forEach(o -> o.onBooked(appointment));
    }

    @Override
    public void notifyRescheduled(Appointment appointment) {
        observers.forEach(o -> o.onRescheduled(appointment));
    }

    @Override
    public void notifyCancelled(Appointment appointment) {
        observers.forEach(o -> o.onCancelled(appointment));
    }

    // ── Strategy setter (allows runtime strategy swap) ─────────────────────

    /**
     * STRATEGY — swap the scheduling algorithm at runtime.
     * OCP: the service is not modified; behaviour changes via a different strategy.
     */
    public void setSchedulingStrategy(AppointmentSchedulingStrategy strategy) {
        this.schedulingStrategy = strategy;
    }

    // ── Core business methods ──────────────────────────────────────────────

    /**
     * Books an appointment using the active {@link AppointmentSchedulingStrategy}
     * to select the appropriate slot, and the {@link AppointmentFactory} to create
     * the appointment entity.
     *
     * After persisting, all registered observers are notified (OBSERVER).
     */
    public Appointment bookAppointment(Long patientId, String patientName,
                                       Integer doctorId, LocalDateTime time, String notes) {

        // STRATEGY — delegate slot selection to the current strategy
        List<AppointmentSlot> available =
                slotRepo.findByDoctorIdAndBookedFalseOrderBySlotTime(doctorId);
        AppointmentSlot slot = schedulingStrategy.selectSlot(time, available);
        if (slot == null) throw new RuntimeException("Slot not available");

        slot.setBooked(true);
        slotRepo.save(slot);

        // FACTORY — delegate object creation to the factory (never call new Appointment() here)
        Appointment a = AppointmentFactory.createScheduledAppointment(patientId, patientName, slot, notes);
        Appointment saved = appointmentRepo.save(a);

        // OBSERVER — notify all registered observers
        notifyBooked(saved);
        return saved;
    }

    /**
     * Reschedules an existing appointment.
     * Uses FACTORY to mutate the entity correctly, OBSERVER to fire the event.
     */
    public Appointment reschedule(Long appointmentId, LocalDateTime newTime) {
        Appointment a = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        if (!a.getStatus().canBeRescheduled())
            throw new RuntimeException("This appointment cannot be rescheduled");

        // STRATEGY — reuse strategy to find the new slot
        List<AppointmentSlot> available =
                slotRepo.findByDoctorIdAndBookedFalseOrderBySlotTime(a.getDoctorId());
        AppointmentSlot newSlot = schedulingStrategy.selectSlot(newTime, available);
        if (newSlot == null) throw new RuntimeException("New slot not available");

        newSlot.setBooked(true);
        slotRepo.save(newSlot);

        // FACTORY — delegate mutation to factory
        Appointment rescheduled = AppointmentFactory.createRescheduledAppointment(a, newSlot);
        Appointment saved = appointmentRepo.save(rescheduled);

        // OBSERVER — notify
        notifyRescheduled(saved);
        return saved;
    }

    /** Cancels an appointment and notifies all observers. */
    public void cancel(Long appointmentId) {
        Appointment a = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        if (!a.getStatus().canBeCancelled())
            throw new RuntimeException("This appointment cannot be cancelled");
        a.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepo.save(a);

        // OBSERVER — notify
        notifyCancelled(saved);
    }

    /** Adds a new bookable slot for a doctor. */
    public AppointmentSlot addSlot(Integer doctorId, LocalDateTime slotTime) {
        AppointmentSlot slot = new AppointmentSlot();
        slot.setDoctorId(doctorId);
        doctorRepo.findById(doctorId).ifPresent(d -> {
            slot.setDoctorName(d.getName());
            slot.setDoctorSpecialty(d.getSpecialization());
        });
        slot.setSlotTime(slotTime);
        return slotRepo.save(slot);
    }

    // ── Query methods ──────────────────────────────────────────────────────

    public List<Appointment> getByPatient(Long patientId) {
        return appointmentRepo.findByPatientIdOrderByAppointmentTimeDesc(patientId);
    }
    public List<Appointment>     getAll()              { return appointmentRepo.findAllByOrderByAppointmentTimeDesc(); }
    public List<AppointmentSlot> getAvailableSlots()   { return slotRepo.findByBookedFalseOrderBySlotTime(); }
    public List<AppointmentSlot> getAvailableSlotsByDoctor(Integer doctorId) {
        return slotRepo.findByDoctorIdAndBookedFalseOrderBySlotTime(doctorId);
    }
    public Appointment getById(Long id) { return appointmentRepo.findById(id).orElse(null); }
}
