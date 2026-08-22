package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Doctor;
import com.smartcare.hms.entity.Patient;
import com.smartcare.hms.entity.Treatment;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.DoctorRepository;
import com.smartcare.hms.repository.PatientRepository;
import com.smartcare.hms.repository.TreatmentRepository;
import com.smartcare.hms.service.TreatmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository,
                                PatientRepository patientRepository,
                                DoctorRepository doctorRepository) {
        this.treatmentRepository = treatmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Treatment recordTreatment(Treatment treatment) {
        if (treatment.getDiagnosis() == null || treatment.getDiagnosis().isBlank()) {
            throw new InvalidInputException("Diagnosis cannot be empty");
        }

        // Resolve Patient from DB
        if (treatment.getPatient() == null || treatment.getPatient().getPatientId() == null) {
            throw new InvalidInputException("Patient ID is required");
        }
        Long patientId = treatment.getPatient().getPatientId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        treatment.setPatient(patient);

        // Resolve Doctor from DB
        if (treatment.getDoctor() == null || treatment.getDoctor().getDoctorId() == null) {
            throw new InvalidInputException("Doctor ID is required");
        }
        Long doctorId = treatment.getDoctor().getDoctorId();
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
        treatment.setDoctor(doctor);

        return treatmentRepository.save(treatment);
    }

    @Override
    public Treatment getTreatmentById(Long id) {
        return treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found with id: " + id));
    }

    @Override
    public List<Treatment> getAllTreatments() {
        return treatmentRepository.findAll();
    }

    @Override
    public List<Treatment> getMedicalHistoryForPatient(Long patientId) {
        return treatmentRepository.findByPatient_PatientId(patientId);
    }

    @Override
    public Treatment updateTreatment(Long id, Treatment updated) {
        Treatment existing = getTreatmentById(id);
        existing.setDiagnosis(updated.getDiagnosis());
        existing.setPrescription(updated.getPrescription());
        existing.setTreatmentNotes(updated.getTreatmentNotes());
        existing.setTreatmentDate(updated.getTreatmentDate());
        return treatmentRepository.save(existing);
    }

    @Override
    public void deleteTreatment(Long id) {
        treatmentRepository.delete(getTreatmentById(id));
    }
}
