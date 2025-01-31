package com.codershubham.cms.cms.controller;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.Course;
import com.codershubham.cms.cms.service.CourseService;
import com.codershubham.cms.cms.service.DepartmentService;
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
        model.addAttribute("course", new Course());  // Initialize a new Course object for the form
        return "courses";  // Thymeleaf template 'courses.html'
    }


    @GetMapping("/add")
    public String addCourseForm(Model model) {
        // Add a list of departments to the model to populate the dropdown
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("course", new Course());  // Add an empty course object for binding
        return "add-course";  // A view to display the form to add a course
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course) {
        courseService.createCourse(course);
        return "redirect:/courses";  // Redirect back to the courses page after creation
    }

    @PostMapping("/update/{courseID}")
    public String updateCourse(@PathVariable int courseID, @ModelAttribute Course updatedCourse) {
        courseService.updateCourse(courseID, updatedCourse);
        return "redirect:/courses";  // Redirect back to the courses page after update
    }

    @GetMapping("/delete/{courseID}")
    public String deleteCourse(@PathVariable int courseID) {
        courseService.deleteCourse(courseID);
        return "redirect:/courses";  // Redirect back to the courses page after deletion
    }
}
