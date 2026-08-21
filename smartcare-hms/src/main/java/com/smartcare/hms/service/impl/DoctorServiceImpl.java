package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Department;
import com.smartcare.hms.entity.Doctor;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.DepartmentRepository;
import com.smartcare.hms.repository.DoctorRepository;
import com.smartcare.hms.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Doctor addDoctor(Doctor doctor) {
        validate(doctor);
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor updated) {
        Doctor existing = getDoctorById(id);
        validate(updated);
        existing.setFullName(updated.getFullName());
        existing.setContactNumber(updated.getContactNumber());
        existing.setQualification(updated.getQualification());
        existing.setSpecialization(updated.getSpecialization());
        existing.setConsultationFee(updated.getConsultationFee());
        return doctorRepository.save(existing);
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor existing = getDoctorById(id);
        doctorRepository.delete(existing);
    }

    @Override
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    public Doctor assignDoctorToDepartment(Long doctorId, Long departmentId) {
        Doctor doctor = getDoctorById(doctorId);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        doctor.setDepartment(department);
        return doctorRepository.save(doctor);
    }

    private void validate(Doctor doctor) {
        if (doctor.getFullName() == null || doctor.getFullName().isBlank()) {
            throw new InvalidInputException("Doctor name cannot be empty");
        }
        if (doctor.getConsultationFee() == null || doctor.getConsultationFee() <= 0) {
            throw new InvalidInputException("Consultation fee must be greater than zero");
        }
    }
}
