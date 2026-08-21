package com.smartcare.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @NotBlank(message = "Department name cannot be empty")
    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;

    @Column(name = "location")
    private String location;

    @OneToOne
    @JoinColumn(name = "head_doctor_id")
    private Doctor headDoctor;

    public Department() {}
    public Department(String departmentName, String location) {
        this.departmentName = departmentName;
        this.location = location;
    }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Doctor getHeadDoctor() { return headDoctor; }
    public void setHeadDoctor(Doctor headDoctor) { this.headDoctor = headDoctor; }
}
