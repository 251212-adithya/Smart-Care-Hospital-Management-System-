package com.smartcare.hms.service;

import com.smartcare.hms.entity.Patient;
import java.util.List;

public interface PatientService {
    Patient registerPatient(Patient patient);
    Patient getPatientById(Long id);
    List<Patient> getAllPatients();
    Patient updatePatient(Long id, Patient patient);
    void deletePatient(Long id);
    List<Patient> searchPatientsByName(String name);
}
