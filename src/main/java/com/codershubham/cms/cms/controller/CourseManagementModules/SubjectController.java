package com.codershubham.cms.cms.controller.CourseManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping(PathConstant.SUBJECTS_PATH)
public class SubjectController {

    @Autowired
    private CourseService courseService;  // Service to fetch courses
    @Autowired
    private SubjectService subjectService;  // Service to handle subject-related operations

    @Autowired
    HttpSession session;

    @Autowired
    private UserRoleUtil userRoleUtil;

    // Get all subjects and display the subject list page
    @GetMapping
    public String getAllSubjects(Model model) {
        // Fetch all subjects
        model.addAttribute("subjects", subjectService.getAllSubjects());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "CourseManagement/subjects/subjects";  // Thymeleaf template for listing subjects
    }

    // Show form for adding a new subject
    @GetMapping(PathConstant.ADD_PATH)
    public String addSubjectForm(Model model) {
        // Fetch all courses for the dropdown
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subject", new SubjectsModel());// Empty subject object for binding

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "CourseManagement/subjects/add-subject";  // Form to add a new subject
    }

    // Create a new subject
    @PostMapping(PathConstant.ADD_PATH)
    public String addSubject(@ModelAttribute SubjectsModel subject) {
        // Set the current date and time for the creation date
        subject.setCreatedAt(LocalDateTime.now());
        // Save the new subject
        subjectService.createSubject(subject);
        return "redirect:/" + PathConstant.SUBJECTS_PATH;   // Redirect to the subjects list page
    }

    // Show form for updating an existing subject
    @GetMapping(PathConstant.UPDATE_PATH)
    public String updateSubjectForm(@PathVariable Long id, Model model) {
        // Fetch the subject by its ID
        SubjectsModel subject = subjectService.getSubjectById(id);
        // Fetch all courses for the dropdown
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subject", subject);  // Add the subject to the model for editing

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "CourseManagement/subjects/update-subject";  // Form to update an existing subject
    }

    // Update an existing subject
    @PostMapping(PathConstant.UPDATE_PATH)
    public String updateSubject(@PathVariable Long id, @ModelAttribute SubjectsModel updatedSubject) {
        // Set the current date and time for the updated date
        updatedSubject.setCreatedAt(LocalDateTime.now());
        // Update the subject
        subjectService.updateSubject(id, updatedSubject);
        return "redirect:/" + PathConstant.SUBJECTS_PATH;   // Redirect to the subjects list page after update
    }

    // Delete a subject
    @GetMapping(PathConstant.DELETE_PATH)
    public String deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);  // Delete the subject
        return "redirect:/" + PathConstant.SUBJECTS_PATH;   // Redirect to the subjects list page after deletion
    }
}
