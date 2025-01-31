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
    private DepartmentService departmentService;  // Service to fetch departments
    @Autowired
    private CourseService courseService;  // Service to fetch courses
    @Autowired
    private SubjectService subjectService;  // Service to handle subject-related operations

    // Get all departments and their associated courses
    @GetMapping("/create")
    public String showCreateSubjectPage(Model model) {
        // Fetch all courses
        List<Course> courses = courseService.getAllCourses();  // Assuming you have a method to get all courses
        // Add the courses to the model
        model.addAttribute("courses", courses);

        return "subjects";  // This should be the template you want to render (createSubject.html)
    }


    // Create a new subject
    @PostMapping("/create")
    public String createSubject(@RequestParam String subjectCode,
                                @RequestParam String subjectName,
                                @RequestParam int credits,
                                @RequestParam Long courseID) {
        // Fetch the course using courseID from the request
        Course course = courseService.getCourseById(courseID);

        // Create a new subject and set the necessary fields
        Subjects subject = new Subjects();
        subject.setSubjectCode(subjectCode);
        subject.setSubjectName(subjectName);
        subject.setCredits(credits);
        subject.setCreatedAt(LocalDateTime.now());
        subject.setCourse(course);

        // Save the new subject
        subjectService.createSubject(subject);

        // Redirect back to the subject creation page (to show newly created subject)
        return "redirect:/subjects/create";
    }

    // Update an existing subject
    @PostMapping("/update/{subjectID}")
    public String updateSubject(@PathVariable Long subjectID,
                                @RequestParam String subjectCode,
                                @RequestParam String subjectName,
                                @RequestParam int credits,
                                @RequestParam Long courseID) {
        // Fetch the course using courseID
        Course course = courseService.getCourseById(courseID);

        // Create a new subject object with updated details
        Subjects updatedSubject = new Subjects();
        updatedSubject.setSubjectid(subjectID);
        updatedSubject.setSubjectCode(subjectCode);
        updatedSubject.setSubjectName(subjectName);
        updatedSubject.setCredits(credits);
        updatedSubject.setCreatedAt(LocalDateTime.now());
        updatedSubject.setCourse(course);

        // Update the subject using the service
        subjectService.updateSubject(subjectID, updatedSubject);

        // Redirect to subject creation page after update
        return "redirect:/subjects/create";  // You can also redirect to a page that lists all subjects if you wish
    }

    // Delete a subject
    @GetMapping("/delete/{subjectID}")
    public String deleteSubject(@PathVariable Long subjectID) {
        subjectService.deleteSubject(subjectID);  // Delete the subject
        return "redirect:/subjects/create";  // Redirect to subject creation page after deletion
    }
}
