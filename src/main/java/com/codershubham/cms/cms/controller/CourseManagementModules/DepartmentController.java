package com.codershubham.cms.cms.controller.CourseManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(PathConstant.DEPARTMENTS_PATH)
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    HttpSession session;

    @Autowired
    private UserRoleUtil userRoleUtil;


    @GetMapping
    public String getAllDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "CourseManagement/departments/departments";
    }

    @GetMapping(PathConstant.ADD_PATH)
    public String add(Model model) {
        model.addAttribute("department", new DepartmentModel());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole); // Add a new Department object to the model
        return "CourseManagement/departments/add-department";
    }

    @PostMapping(PathConstant.ADD_PATH)
    public String addDepartment(@ModelAttribute DepartmentModel departmentModel) {
        departmentService.addDepartment(departmentModel);
        return "redirect:/" + PathConstant.DEPARTMENTS_PATH;
    }

    @GetMapping(PathConstant.UPDATE_PATH)
    public String updateDepartment(@PathVariable Long id, Model model) {
        // Fetch the department from the database using the provided id
        DepartmentModel departmentModel = departmentService.getDepartmentById(id);

        // Add the department object to the model
        model.addAttribute("department", departmentModel);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        // Return the view name (the form where the department data will be updated)
        return "CourseManagement/departments/edit-department";
    }

    @PostMapping(PathConstant.UPDATE_PATH)
    public String updateDepartment(@PathVariable Long id, @ModelAttribute DepartmentModel departmentModel) {
        departmentService.updateDepartment(id, departmentModel);
        return "redirect:/" + PathConstant.DEPARTMENTS_PATH;
    }

    @GetMapping(PathConstant.DELETE_PATH)
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/" + PathConstant.DEPARTMENTS_PATH;
    }
}