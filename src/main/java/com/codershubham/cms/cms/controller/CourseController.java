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
        return "courses/courses";  // Thymeleaf template 'courses.html'
    }

    @GetMapping("/add")
    public String addCourseForm(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());  // List of departments for dropdown
        model.addAttribute("course", new Course());  // Empty Course object for binding
        return "courses/add-course";  // Form to add a new course
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course) {
        courseService.createCourse(course);
        return "redirect:/courses";  // Redirect back to the courses page after creation
    }

    @GetMapping("/update/{courseID}")
    public String updateCourseForm(@PathVariable Long courseID, Model model) {
        Course course = courseService.getCourseById(courseID);
        model.addAttribute("departments", departmentService.getAllDepartments());  // List of departments for dropdown
        model.addAttribute("course", course);  // Add the course to be updated
        return "courses/update-course";  // Form to update a course
    }

    @PostMapping("/update/{courseID}")
    public String updateCourse(@PathVariable Long courseID, @ModelAttribute Course updatedCourse) {
        courseService.updateCourse(courseID, updatedCourse);
        return "redirect:/courses";  // Redirect back to the courses page after update
    }

    @GetMapping("/delete/{courseID}")
    public String deleteCourse(@PathVariable int courseID) {
        courseService.deleteCourse(courseID);
        return "redirect:/courses";  // Redirect back to the courses page after deletion
    }
}
