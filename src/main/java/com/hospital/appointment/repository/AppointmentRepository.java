package com.hospital.appointment.repository;
import com.hospital.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdOrderByAppointmentTimeDesc(Long patientId);
    List<Appointment> findByDoctorIdOrderByAppointmentTimeDesc(Integer doctorId);
    List<Appointment> findAllByOrderByAppointmentTimeDesc();
}
