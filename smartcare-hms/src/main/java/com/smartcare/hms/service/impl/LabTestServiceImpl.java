package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.LabTest;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.LabTestRepository;
import com.smartcare.hms.service.LabTestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;

    public LabTestServiceImpl(LabTestRepository labTestRepository) {
        this.labTestRepository = labTestRepository;
    }

    @Override
    public LabTest addLabTest(LabTest labTest) {
        if (labTest.getTestName() == null || labTest.getTestName().isBlank()) {
            throw new InvalidInputException("Test name cannot be empty");
        }
        labTest.setTestStatus(LabTest.TestStatus.PENDING);
        return labTestRepository.save(labTest);
    }

    @Override
    public LabTest updateLabResult(Long id, String testResult, LabTest.TestStatus status) {
        LabTest existing = getLabTestById(id);
        existing.setTestResult(testResult);
        existing.setTestStatus(status);
        return labTestRepository.save(existing);
    }

    @Override
    public LabTest getLabTestById(Long id) {
        return labTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab test not found with id: " + id));
    }

    @Override
    public List<LabTest> getAllLabTests() {
        return labTestRepository.findAll();
    }

    @Override
    public List<LabTest> getHistoryForPatient(Long patientId) {
        return labTestRepository.findByPatient_PatientId(patientId);
    }
}
