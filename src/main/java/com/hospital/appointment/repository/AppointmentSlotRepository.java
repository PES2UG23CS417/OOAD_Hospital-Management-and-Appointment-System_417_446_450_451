package com.hospital.appointment.repository;
import com.hospital.appointment.model.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    List<AppointmentSlot> findByBookedFalseOrderBySlotTime();
    List<AppointmentSlot> findByDoctorIdAndBookedFalseOrderBySlotTime(Integer doctorId);
}
