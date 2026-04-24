package com.hospital.billing.repository;
import com.hospital.billing.model.Bill;
import com.hospital.billing.model.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<Bill> findByStatus(BillStatus status);
    List<Bill> findAllByOrderByCreatedAtDesc();
}
