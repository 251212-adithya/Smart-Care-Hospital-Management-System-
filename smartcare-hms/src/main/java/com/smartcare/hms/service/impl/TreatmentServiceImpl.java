package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Treatment;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.TreatmentRepository;
import com.smartcare.hms.service.TreatmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public Treatment recordTreatment(Treatment treatment) {
        if (treatment.getDiagnosis() == null || treatment.getDiagnosis().isBlank()) {
            throw new InvalidInputException("Diagnosis cannot be empty");
        }
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
