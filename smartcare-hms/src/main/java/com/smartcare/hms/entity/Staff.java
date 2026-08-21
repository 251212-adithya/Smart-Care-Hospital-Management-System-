package com.smartcare.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "staff")
public class Staff extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;

    @NotBlank(message = "Role title is required")
    @Column(name = "role_title", nullable = false)
    private String roleTitle;

    public Staff() { super(); }
    public Staff(String fullName, String contactNumber, String roleTitle) {
        super(fullName, contactNumber);
        this.roleTitle = roleTitle;
    }

    @Override
    public String getRole() { return roleTitle == null ? "STAFF" : roleTitle.toUpperCase(); }

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getRoleTitle() { return roleTitle; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }
}
