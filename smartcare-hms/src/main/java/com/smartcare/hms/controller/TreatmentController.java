package com.smartcare.hms.controller;

import com.smartcare.hms.entity.Treatment;
import com.smartcare.hms.service.TreatmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @PostMapping
    public ResponseEntity<Treatment> record(@Valid @RequestBody Treatment treatment) {
        return new ResponseEntity<>(treatmentService.recordTreatment(treatment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Treatment getById(@PathVariable Long id) {
        return treatmentService.getTreatmentById(id);
    }

    @GetMapping
    public List<Treatment> getAll() {
        return treatmentService.getAllTreatments();
    }

    @GetMapping("/patient/{patientId}/history")
    public List<Treatment> medicalHistory(@PathVariable Long patientId) {
        return treatmentService.getMedicalHistoryForPatient(patientId);
    }

    @PutMapping("/{id}")
    public Treatment update(@PathVariable Long id, @Valid @RequestBody Treatment treatment) {
        return treatmentService.updateTreatment(id, treatment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        treatmentService.deleteTreatment(id);
        return ResponseEntity.noContent().build();
    }
}
