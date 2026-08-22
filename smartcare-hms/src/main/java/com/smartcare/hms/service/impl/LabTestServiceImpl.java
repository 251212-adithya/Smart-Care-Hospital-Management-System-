package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Doctor;
import com.smartcare.hms.entity.LabTest;
import com.smartcare.hms.entity.Patient;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.DoctorRepository;
import com.smartcare.hms.repository.LabTestRepository;
import com.smartcare.hms.repository.PatientRepository;
import com.smartcare.hms.service.LabTestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public LabTestServiceImpl(LabTestRepository labTestRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.labTestRepository = labTestRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public LabTest addLabTest(LabTest labTest) {
        if (labTest.getTestName() == null || labTest.getTestName().isBlank()) {
            throw new InvalidInputException("Test name cannot be empty");
        }

        // Resolve Patient from DB
        if (labTest.getPatient() == null || labTest.getPatient().getPatientId() == null) {
            throw new InvalidInputException("Patient ID is required");
        }
        Long patientId = labTest.getPatient().getPatientId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        labTest.setPatient(patient);

        // Resolve Doctor from DB (optional field)
        if (labTest.getRequestedByDoctor() != null && labTest.getRequestedByDoctor().getDoctorId() != null) {
            Long doctorId = labTest.getRequestedByDoctor().getDoctorId();
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
            labTest.setRequestedByDoctor(doctor);
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
