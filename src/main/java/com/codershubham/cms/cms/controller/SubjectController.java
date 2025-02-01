package com.codershubham.cms.cms.controller;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.Course;
import com.codershubham.cms.cms.model.Subjects;
import com.codershubham.cms.cms.service.CourseService;
import com.codershubham.cms.cms.service.DepartmentService;
import com.codershubham.cms.cms.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(PathConstant.SUBJECTS_PATH)
public class SubjectController {

    @Autowired
    private CourseService courseService;  // Service to fetch courses
    @Autowired
    private SubjectService subjectService;  // Service to handle subject-related operations

    // Get all subjects and display the subject list page
    @GetMapping
    public String getAllSubjects(Model model) {
        // Fetch all subjects
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "subjects/subjects";  // Thymeleaf template for listing subjects
    }

    // Show form for adding a new subject
    @GetMapping("/add")
    public String addSubjectForm(Model model) {
        // Fetch all courses for the dropdown
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subject", new Subjects());  // Empty subject object for binding
        return "subjects/add-subject";  // Form to add a new subject
    }

    // Create a new subject
    @PostMapping("/add")
    public String addSubject(@ModelAttribute Subjects subject) {
        // Set the current date and time for the creation date
        subject.setCreatedAt(LocalDateTime.now());
        // Save the new subject
        subjectService.createSubject(subject);
        return "redirect:/subjects";  // Redirect to the subjects list page
    }

    // Show form for updating an existing subject
    @GetMapping("/update/{subjectID}")
    public String updateSubjectForm(@PathVariable Long subjectID, Model model) {
        // Fetch the subject by its ID
        Subjects subject = subjectService.getSubjectById(subjectID);
        // Fetch all courses for the dropdown
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subject", subject);  // Add the subject to the model for editing
        return "subjects/update-subject";  // Form to update an existing subject
    }

    // Update an existing subject
    @PostMapping("/update/{subjectID}")
    public String updateSubject(@PathVariable Long subjectID, @ModelAttribute Subjects updatedSubject) {
        // Set the current date and time for the updated date
        updatedSubject.setCreatedAt(LocalDateTime.now());
        // Update the subject
        subjectService.updateSubject(subjectID, updatedSubject);
        return "redirect:/subjects";  // Redirect to the subjects list page after update
    }

    // Delete a subject
    @GetMapping("/delete/{subjectID}")
    public String deleteSubject(@PathVariable Long subjectID) {
        subjectService.deleteSubject(subjectID);  // Delete the subject
        return "redirect:/subjects";  // Redirect to the subjects list page after deletion
    }
}
