package com.codershubham.cms.cms.controller.StudentManagementModules;


import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.DTO.AssignStudentsRequestDto;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentEnrollmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentEnrollmentService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(PathConstant.ENROLLMENTS_PATH)
public class StudentEnrollmentController {

    @Autowired
    private StudentEnrollmentService studentEnrollmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private HttpSession session;

    @GetMapping("/assign-students-to-division/{semesterId}")
    public String showAssignStudentsPage(@PathVariable Long semesterId, Model model) {
        // Check if semesterId is valid (example: non-negative, exists in the database)
        if (semesterId == null || semesterId <= 0) {
            model.addAttribute("error", "Invalid Semester ID.");
            return "error"; // Return to an error page (you should have an error.html page)
        }

        // Fetch semester details
        SemesterModel semester = semesterService.getSemesterById(semesterId);
        if (semester == null) {
            model.addAttribute("error", "Semester not found.");
            return "error"; // Return to an error page
        }

        // Fetch divisions for the given semester
        List<DivisionModel> divisions = divisionService.getDivisionsBySemester(semesterId);
        if (divisions.isEmpty()) {
            model.addAttribute("error", "No divisions found for the selected semester.");
            return "error"; // Return to an error page
        }

        // Fetch unassigned students based on the semester's course
        List<StudentModel> unassignedStudents = studentService.getUnassignedStudentsBySemester(semesterId);
        if (unassignedStudents.isEmpty()) {
            model.addAttribute("error", "No unassigned students found for the selected semester.");
            return "error"; // Return to an error page
        }

        // Add attributes to model
        model.addAttribute("semesterId", semesterId);
        model.addAttribute("divisions", divisions);
        model.addAttribute("unassignedStudents", unassignedStudents);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/students/assign-students-to-division"; // Thymeleaf template
    }

    @PostMapping("/assign")
    public String assignStudents(@RequestBody AssignStudentsRequestDto request) {
        SemesterModel semester = new SemesterModel();
        semester.setId(request.getSemesterId());

        DivisionModel division = new DivisionModel();
        division.setId(request.getDivisionId());

        for (Long studentId : request.getStudentIds()) {
            StudentModel student = new StudentModel();
            student.setId(studentId);

            StudentEnrollmentModel enrollment = new StudentEnrollmentModel();
            enrollment.setStudent(student);
            enrollment.setSemester(semester);
            enrollment.setDivision(division);

            studentEnrollmentService.enrollStudent(enrollment);
        }
        return "redirect:/students/select";
//        return "Students assigned successfully!";
    }
}
