package com.smartcare.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "lab_tests")
public class LabTest {

    public enum TestStatus { PENDING, IN_PROGRESS, COMPLETED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_test_id")
    private Long labTestId;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_doctor_id")
    private Doctor requestedByDoctor;

    @NotBlank(message = "Test name cannot be empty")
    @Column(name = "test_name", nullable = false)
    private String testName;

    @NotNull
    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(name = "test_result")
    private String testResult;

    @Column(name = "technician_name")
    private String technicianName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "test_status", nullable = false, length = 20)
    private TestStatus testStatus = TestStatus.PENDING;

    public LabTest() {}

    public Long getLabTestId() { return labTestId; }
    public void setLabTestId(Long labTestId) { this.labTestId = labTestId; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Doctor getRequestedByDoctor() { return requestedByDoctor; }
    public void setRequestedByDoctor(Doctor requestedByDoctor) { this.requestedByDoctor = requestedByDoctor; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public LocalDate getTestDate() { return testDate; }
    public void setTestDate(LocalDate testDate) { this.testDate = testDate; }
    public String getTestResult() { return testResult; }
    public void setTestResult(String testResult) { this.testResult = testResult; }
    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }
    public TestStatus getTestStatus() { return testStatus; }
    public void setTestStatus(TestStatus testStatus) { this.testStatus = testStatus; }
}
