package com.codershubham.cms.cms.controller;


import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.Department;
import com.codershubham.cms.cms.service.DepartmentService;
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
        return "departments";
    }

    @PostMapping("/add")
    public String addDepartment(@ModelAttribute Department department) {
        departmentService.addDepartment(department);
        return "redirect:/departments";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute Department department) {
        departmentService.updateDepartment(id, department);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}