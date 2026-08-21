package com.smartcare.hms.repository;

import com.smartcare.hms.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    List<Treatment> findByPatient_PatientId(Long patientId);
}
