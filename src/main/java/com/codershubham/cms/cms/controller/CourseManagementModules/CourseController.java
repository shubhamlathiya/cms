package com.codershubham.cms.cms.controller.CourseManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(PathConstant.COURSES_PATH)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    HttpSession session;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @GetMapping
    public String getAllCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "CourseManagement/courses/courses";  // Thymeleaf template 'courses.html'
    }

    @GetMapping(PathConstant.ADD_PATH)
    public String addCourseForm(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());  // List of departments for dropdown
        model.addAttribute("course", new CourseModel());  // Empty Course object for binding
        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        return "CourseManagement/courses/add-course";  // Form to add a new course
    }

    @PostMapping(PathConstant.ADD_PATH)
    public String addCourse(@ModelAttribute CourseModel courseModel) {
        courseService.createCourse(courseModel);
        return "redirect:/" + PathConstant.COURSES_PATH;  // Redirect back to the courses page after creation
    }

    @GetMapping(PathConstant.UPDATE_PATH)
    public String updateCourseForm(@PathVariable Long id, Model model) {
        CourseModel courseModel = courseService.getCourseById(id);
        model.addAttribute("departments", departmentService.getAllDepartments());  // List of departments for dropdown
        model.addAttribute("course", courseModel);  // Add the course to be updated
        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        return "CourseManagement/courses/update-course";  // Form to update a course
    }

    @PostMapping(PathConstant.UPDATE_PATH)
    public String updateCourse(@PathVariable Long id, @ModelAttribute CourseModel updatedCourseModel) {
        courseService.updateCourse(id, updatedCourseModel);
        return "redirect:/" + PathConstant.COURSES_PATH;   // Redirect back to the courses page after update
    }

    @GetMapping(PathConstant.DELETE_PATH)
    public String deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return "redirect:/" + PathConstant.COURSES_PATH;   // Redirect back to the courses page after deletion
    }
}
