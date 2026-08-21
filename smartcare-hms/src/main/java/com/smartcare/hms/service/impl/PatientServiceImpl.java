package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Patient;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.PatientRepository;
import com.smartcare.hms.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient registerPatient(Patient patient) {
        validate(patient);
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient updatePatient(Long id, Patient updated) {
        Patient existing = getPatientById(id);
        validate(updated);
        existing.setFullName(updated.getFullName());
        existing.setContactNumber(updated.getContactNumber());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setGender(updated.getGender());
        existing.setAddress(updated.getAddress());
        existing.setBloodGroup(updated.getBloodGroup());
        existing.setEmergencyContact(updated.getEmergencyContact());
        return patientRepository.save(existing);
    }

    @Override
    public void deletePatient(Long id) {
        Patient existing = getPatientById(id);
        patientRepository.delete(existing);
    }

    @Override
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByFullNameContainingIgnoreCase(name);
    }

    private void validate(Patient patient) {
        if (patient.getFullName() == null || patient.getFullName().isBlank()) {
            throw new InvalidInputException("Patient name cannot be empty");
        }
        if (patient.getContactNumber() == null || patient.getContactNumber().isBlank()) {
            throw new InvalidInputException("Contact number cannot be empty");
        }
    }
}
