package com.codershubham.cms.cms.controller.CourseManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
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

    @GetMapping
    public String getAllCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("departments", departmentService.getAllDepartments());  // Pass list of departments
        return "CourseManagement/courses/courses";  // Thymeleaf template 'courses.html'
    }

    @GetMapping("/add")
    public String addCourseForm(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());  // List of departments for dropdown
        model.addAttribute("course", new CourseModel());  // Empty Course object for binding
        return "CourseManagement/courses/add-course";  // Form to add a new course
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute CourseModel courseModel) {
        courseService.createCourse(courseModel);
        return "redirect:/courses";  // Redirect back to the courses page after creation
    }

    @GetMapping("/update/{courseID}")
    public String updateCourseForm(@PathVariable Long courseID, Model model) {
        CourseModel courseModel = courseService.getCourseById(courseID);
        model.addAttribute("departments", departmentService.getAllDepartments());  // List of departments for dropdown
        model.addAttribute("course", courseModel);  // Add the course to be updated
        return "CourseManagement/courses/update-course";  // Form to update a course
    }

    @PostMapping("/update/{courseID}")
    public String updateCourse(@PathVariable Long courseID, @ModelAttribute CourseModel updatedCourseModel) {
        courseService.updateCourse(courseID, updatedCourseModel);
        return "redirect:/courses";  // Redirect back to the courses page after update
    }

    @GetMapping("/delete/{courseID}")
    public String deleteCourse(@PathVariable int courseID) {
        courseService.deleteCourse(courseID);
        return "redirect:/courses";  // Redirect back to the courses page after deletion
    }
}
