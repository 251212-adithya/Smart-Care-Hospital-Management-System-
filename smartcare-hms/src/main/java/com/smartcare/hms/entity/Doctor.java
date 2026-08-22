package com.smartcare.hms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "doctors")
public class Doctor extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @NotBlank(message = "Qualification is required")
    @Column(name = "qualification", nullable = false)
    private String qualification;

    @NotBlank(message = "Specialization is required")
    @Column(name = "specialization", nullable = false)
    private String specialization;

    @NotNull(message = "Consultation fee is required")
    @DecimalMin(value = "0.01", message = "Consultation fee must be greater than zero")
    @Column(name = "consultation_fee", nullable = false)
    private Double consultationFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "headDoctor"})
    private Department department;

    public Doctor() { super(); }

    public Doctor(String fullName, String contactNumber, String qualification,
                  String specialization, Double consultationFee, Department department) {
        super(fullName, contactNumber);
        this.qualification = qualification;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.department = department;
    }

    @Override
    public String getRole() { return "DOCTOR"; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
