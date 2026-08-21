package com.smartcare.hms.service;

import com.smartcare.hms.entity.Department;
import java.util.List;

public interface DepartmentService {
    Department addDepartment(Department department);
    Department getDepartmentById(Long id);
    List<Department> getAllDepartments();
    Department updateDepartment(Long id, Department department);
    void deleteDepartment(Long id);
}
