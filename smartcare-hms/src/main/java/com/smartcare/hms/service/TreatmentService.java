package com.smartcare.hms.service;

import com.smartcare.hms.entity.Treatment;
import java.util.List;

public interface TreatmentService {
    Treatment recordTreatment(Treatment treatment);
    Treatment getTreatmentById(Long id);
    List<Treatment> getAllTreatments();
    List<Treatment> getMedicalHistoryForPatient(Long patientId);
    Treatment updateTreatment(Long id, Treatment treatment);
    void deleteTreatment(Long id);
}
