package com.smartcare.hms.repository;

import com.smartcare.hms.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPaymentStatus(Bill.PaymentStatus status);
    List<Bill> findByPatient_PatientId(Long patientId);
}
