package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Department;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.DepartmentRepository;
import com.smartcare.hms.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department addDepartment(Department department) {
        if (department.getDepartmentName() == null || department.getDepartmentName().isBlank()) {
            throw new InvalidInputException("Department name cannot be empty");
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department updateDepartment(Long id, Department updated) {
        Department existing = getDepartmentById(id);
        existing.setDepartmentName(updated.getDepartmentName());
        existing.setLocation(updated.getLocation());
        if (updated.getHeadDoctor() != null) {
            existing.setHeadDoctor(updated.getHeadDoctor());
        }
        return departmentRepository.save(existing);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department existing = getDepartmentById(id);
        departmentRepository.delete(existing);
    }
}
