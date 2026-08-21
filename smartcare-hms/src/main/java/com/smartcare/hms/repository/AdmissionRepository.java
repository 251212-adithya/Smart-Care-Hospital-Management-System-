package com.smartcare.hms.repository;

import com.smartcare.hms.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByPatient_PatientId(Long patientId);
    List<Admission> findByAdmissionStatus(Admission.AdmissionStatus status);
}
