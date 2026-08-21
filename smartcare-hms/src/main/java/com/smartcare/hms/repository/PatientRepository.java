package com.smartcare.hms.repository;

import com.smartcare.hms.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByFullNameContainingIgnoreCase(String name);
    List<Patient> findByBloodGroupIgnoreCase(String bloodGroup);
}
