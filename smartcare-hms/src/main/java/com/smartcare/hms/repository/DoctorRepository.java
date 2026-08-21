package com.smartcare.hms.repository;

import com.smartcare.hms.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByFullNameContainingIgnoreCase(String name);
    List<Doctor> findBySpecializationIgnoreCase(String specialization);
    List<Doctor> findByDepartment_DepartmentId(Long departmentId);
}
