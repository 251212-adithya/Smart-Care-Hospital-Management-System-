package com.smartcare.hms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@MappedSuperclass
public abstract class Person {

    @NotBlank(message = "Full name cannot be empty")
    @Column(name = "full_name", nullable = false)
    protected String fullName;

    @NotBlank(message = "Contact number cannot be empty")
    @Pattern(regexp = "^[0-9+\\-() ]{7,15}$", message = "Contact number is not valid")
    @Column(name = "contact_number", nullable = false)
    protected String contactNumber;

    protected Person() {}

    protected Person(String fullName, String contactNumber) {
        this.fullName = fullName;
        this.contactNumber = contactNumber;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public abstract String getRole();
}
