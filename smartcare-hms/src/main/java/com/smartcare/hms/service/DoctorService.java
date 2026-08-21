package com.smartcare.hms.service;

import com.smartcare.hms.entity.Doctor;
import java.util.List;

public interface DoctorService {
    Doctor addDoctor(Doctor doctor);
    Doctor getDoctorById(Long id);
    List<Doctor> getAllDoctors();
    Doctor updateDoctor(Long id, Doctor doctor);
    void deleteDoctor(Long id);
    List<Doctor> searchDoctorsByName(String name);
    Doctor assignDoctorToDepartment(Long doctorId, Long departmentId);
}
