package com.smartcare.hms.repository;

import com.smartcare.hms.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    List<LabTest> findByPatient_PatientId(Long patientId);
    List<LabTest> findByTestDateBetween(LocalDate start, LocalDate end);
}
