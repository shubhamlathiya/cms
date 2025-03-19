package com.codershubham.cms.cms.controller.FacultyManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultySubjectAssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultySubjectAssignmentService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.util.EmailUtil;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(PathConstant.FACULTY_PATH)
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultySubjectAssignmentService facultySubjectAssignmentService;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    HttpSession session;

    @GetMapping(PathConstant.DASHBOARD_PATH)
    public String studentDashboard(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        // Fetch the student by ID
//        StudentModel student = studentService.findById(studentId.getId());
        session.setAttribute("facultyId", faculty.getFacultyId());
        // Check if the student exists
        if (faculty == null) {
            // Handle the case where the student is not found (e.g., redirect to an error page)
            return "error/404"; // You can create a 404 error page
        }

        // Add the student to the model
        model.addAttribute("faculty", faculty);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        // Return the view name
        return "FacultyManagement/faculty-dashboard"; // Ensure this matches your Thymeleaf template name
    }

    @GetMapping("/event")
    public String event(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        model.addAttribute("faculty", faculty);
        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        return "FacultyManagement/calendar";
    }


    @GetMapping("/subjects/{facultyId}")
    public String getFacultySubjects(@PathVariable Long facultyId, Model model) {
        List<FacultySubjectAssignmentModel> assignedSubjects = facultySubjectAssignmentService.getSubjectsByFaculty(facultyId);
        model.addAttribute("assignedSubjects", assignedSubjects);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        Long userId = (Long) session.getAttribute("userId");

        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        model.addAttribute("faculty", faculty);
        return "FacultyManagement/faculty/faculty-subjects"; // Renders faculty-subjects.html
    }

    @GetMapping("/students/list/{divisionId}")
    public ResponseEntity<List<StudentModel>> getStudentsByDivision(@PathVariable Long divisionId) {
        List<StudentModel> students = studentService.getStudentsByDivision(divisionId);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmails(@RequestParam List<String> emails, @RequestParam String message, @RequestParam(required = false) MultipartFile file) {

        for (String email : emails) {
            emailUtil.sendEmailWithAttachment(email, "Faculty Notification", message, file);
        }

        return ResponseEntity.ok("Emails sent successfully to all recipients!");
    }

    @GetMapping("/classes")
    public String showClasses(Model model) {
        Long userId = (Long) session.getAttribute("userId");
        // Get the logged-in faculty's ID from the session
        Long facultyId = (Long) session.getAttribute("facultyId");
        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        // Fetch divisions for the faculty
        List<DivisionModel> divisions = facultyService.getDivisionsByFacultyId(facultyId);
        model.addAttribute("divisions", divisions);
        model.addAttribute("faculty", faculty);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        return "FacultyManagement/faculty-classes"; // Return the classes view template
    }

}
