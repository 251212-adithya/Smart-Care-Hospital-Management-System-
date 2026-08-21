package com.smartcare.hms.controller;

import com.smartcare.hms.entity.Admission;
import com.smartcare.hms.service.AdmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;

    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    @PostMapping
    public ResponseEntity<Admission> admit(@Valid @RequestBody Admission admission) {
        return new ResponseEntity<>(admissionService.admitPatient(admission), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/discharge")
    public Admission discharge(@PathVariable Long id) {
        return admissionService.dischargePatient(id);
    }

    @GetMapping("/{id}")
    public Admission getById(@PathVariable Long id) {
        return admissionService.getAdmissionById(id);
    }

    @GetMapping
    public List<Admission> getAll() {
        return admissionService.getAllAdmissions();
    }
}
