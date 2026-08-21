package com.smartcare.hms.service;

import com.smartcare.hms.entity.LabTest;
import java.util.List;

public interface LabTestService {
    LabTest addLabTest(LabTest labTest);
    LabTest updateLabResult(Long id, String testResult, LabTest.TestStatus status);
    LabTest getLabTestById(Long id);
    List<LabTest> getAllLabTests();
    List<LabTest> getHistoryForPatient(Long patientId);
}
