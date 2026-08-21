package com.smartcare.hms.controller;

import com.smartcare.hms.entity.LabTest;
import com.smartcare.hms.service.LabTestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lab-tests")
public class LabTestController {

    private final LabTestService labTestService;

    public LabTestController(LabTestService labTestService) {
        this.labTestService = labTestService;
    }

    @PostMapping
    public ResponseEntity<LabTest> add(@Valid @RequestBody LabTest labTest) {
        return new ResponseEntity<>(labTestService.addLabTest(labTest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/result")
    public LabTest updateResult(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String result = payload.get("testResult");
        LabTest.TestStatus status = LabTest.TestStatus.valueOf(payload.getOrDefault("testStatus", "COMPLETED"));
        return labTestService.updateLabResult(id, result, status);
    }

    @GetMapping("/{id}")
    public LabTest getById(@PathVariable Long id) {
        return labTestService.getLabTestById(id);
    }

    @GetMapping
    public List<LabTest> getAll() {
        return labTestService.getAllLabTests();
    }

    @GetMapping("/patient/{patientId}/history")
    public List<LabTest> history(@PathVariable Long patientId) {
        return labTestService.getHistoryForPatient(patientId);
    }
}
