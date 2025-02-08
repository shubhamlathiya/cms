package com.codershubham.cms.cms.controller.StudentManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SemesterController {

    @Autowired
    private SemesterService semesterService;


    @Autowired
    private CourseService courseService;


    // Show the form to create a semester for a course
    @GetMapping("/create-semester")
    public String showCreateSemesterForm(Model model) {
        model.addAttribute("semester", new SemesterModel());
        model.addAttribute("courses", courseService.getAllCourses()); // All available courses
        return "StudentManagement/semester/create-semester"; // A view to create a semester
    }

    // Handle the form submission to create a semester for the selected course
    @PostMapping("/create-semester")
    public String createSemester(@ModelAttribute SemesterModel semester, @RequestParam Long courseId, Model model) {
        CourseModel course = courseService.getCourseById(courseId); // Fetch the selected course
        semester.setCourse(course); // Link the semester to the course

        SemesterModel createdSemester = semesterService.createSemester(semester); // Save the semester
        model.addAttribute("semester", createdSemester);
        return "redirect:/create-semester";  // Show success page after creating the semester
    }

    @GetMapping("/semesters")
    public String listSemesters(Model model) {
        List<SemesterModel> semesters = semesterService.findAll();
        System.out.println(semesters);
        model.addAttribute("semesters", semesters);
        return "StudentManagement/semester/semester-list"; // Returns the semester list view
    }

    @GetMapping("/create-division-page")
    public String showCreateDivisionPage(@RequestParam Long semesterId, Model model) {
        model.addAttribute("semesterId", semesterId);
        return "StudentManagement/semester/create-division"; // Returns the division creation view
    }

    // Method to view all divisions for a specific semester
//    @GetMapping("/semester/{semesterId}")
//    public String viewSemesterDivisions(@RequestParam Long semesterId, Model model) {
//
//        // Fetch the semester object by semesterId
//        SemesterModel semester = semesterService.findById(semesterId);
//
//        // Fetch all divisions for the semester
//        List<DivisionModel> divisions = divisionService.findBySemesterId(semesterId);
//        System.out.println(divisions);
//        // Add the semester and divisions to the model
//        model.addAttribute("semester", semester);
//        model.addAttribute("divisions", divisions);
//
//        return "StudentManagement/semester/semester-view"; // Return the view that will display the semester and its divisions
//    }

}
