package com.hospital.doctor.repository;
import com.hospital.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.specialization) LIKE LOWER(CONCAT('%',:spec,'%'))")
    List<Doctor> findBySpecializationIgnoreCase(@Param("spec") String spec);
    List<Doctor> findByAvailabilityStatus(boolean status);
}
