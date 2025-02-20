package com.codershubham.cms.cms.controller.FacultyManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultySubjectAssignmentModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultySubjectAssignmentService;
import com.codershubham.cms.cms.util.EmailUtil;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(PathConstant.FACULTY_PATH)
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FacultySubjectAssignmentService facultySubjectAssignmentService;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    HttpSession session;

    @GetMapping("/dashboard")
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

    // 1️⃣ Show all faculties
    @GetMapping
    public String getAllFaculties(Model model) {
        List<FacultyModel> faculties = facultyService.getAllFaculties();
        model.addAttribute("faculties", faculties);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "FacultyManagement/faculty/faculty-list";
    }

    // 4️⃣ Show update faculty form
    @GetMapping("/edit/{id}")
    public String showEditFacultyForm(@PathVariable Long id, Model model) {
        Optional<FacultyModel> faculty = facultyService.getFacultyById(id);
        if (faculty.isPresent()) {
            model.addAttribute("faculty", faculty.get());
            model.addAttribute("departments", departmentService.getAllDepartments()); // Load departments for selection

            String userRole = userRoleUtil.getUserRole(session);
            model.addAttribute("userRole", userRole);

            return "FacultyManagement/faculty/edit-faculty";
        } else {
            return "redirect:/faculty/list"; // Redirect if faculty not found
        }
    }

    // 5️⃣ Update faculty details
    @PostMapping("/update/{id}")
    public String updateFaculty(@PathVariable Long id, @ModelAttribute FacultyModel faculty) {
        facultyService.updateFaculty(id, faculty);
        return "redirect:/faculty";
    }

    // 6️⃣ Delete faculty
    @GetMapping("/delete/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return "redirect:/faculty"; // Redirect after deleting
    }

    @GetMapping("/subjects/{facultyId}")
    public String getFacultySubjects(@PathVariable Long facultyId, Model model) {
        List<FacultySubjectAssignmentModel> assignedSubjects = facultySubjectAssignmentService.getSubjectsByFaculty(facultyId);
        model.addAttribute("assignedSubjects", assignedSubjects);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        Long userId = (Long) session.getAttribute("userId");

        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        // Add the student to the model
        model.addAttribute("faculty", faculty);
        return "FacultyManagement/faculty/faculty-subjects"; // Renders faculty-subjects.html
    }

}
