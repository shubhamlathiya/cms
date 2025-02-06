package com.codershubham.cms.cms.controller.CourseManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(PathConstant.DEPARTMENTS_PATH)
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public String getAllDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "CourseManagement/departments/departments";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("department", new DepartmentModel()); // Add a new Department object to the model
        return "CourseManagement/departments/add-department";
    }

    @PostMapping("/add")
    public String addDepartment(@ModelAttribute DepartmentModel departmentModel) {
        departmentService.addDepartment(departmentModel);
        return "redirect:/" + PathConstant.DEPARTMENTS_PATH;
    }

    @GetMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id, Model model) {
        // Fetch the department from the database using the provided id
        DepartmentModel departmentModel = departmentService.getDepartmentById(id);

        // Add the department object to the model
        model.addAttribute("department", departmentModel);

        // Return the view name (the form where the department data will be updated)
        return "CourseManagement/departments/edit-department";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute DepartmentModel departmentModel) {
        departmentService.updateDepartment(id, departmentModel);
        return "redirect:/" + PathConstant.DEPARTMENTS_PATH;
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/" + PathConstant.DEPARTMENTS_PATH;
    }
}