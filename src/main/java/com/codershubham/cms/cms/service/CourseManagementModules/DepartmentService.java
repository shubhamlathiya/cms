package com.codershubham.cms.cms.service.CourseManagementModules;


import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public DepartmentModel addDepartment(DepartmentModel departmentModel) {
        return departmentRepository.save(departmentModel);
    }

    // Method to get a department by its ID
    public DepartmentModel getDepartmentById(Long id) {
        // You may want to handle cases where the department is not found
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found with id " + id));
    }

    public DepartmentModel updateDepartment(Long id, DepartmentModel departmentModelDetails) {
        DepartmentModel departmentModel = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        departmentModel.setName(departmentModelDetails.getName());
        departmentModel.setDepartmentHead(departmentModelDetails.getDepartmentHead());
        return departmentRepository.save(departmentModel);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public List<DepartmentModel> getAllDepartments() {
        return departmentRepository.findAll();
    }
}