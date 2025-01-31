package com.codershubham.cms.cms.service;



import com.codershubham.cms.cms.model.Department;
import com.codershubham.cms.cms.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        department.setName(departmentDetails.getName());
        department.setDepartmentHead(departmentDetails.getDepartmentHead());
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}