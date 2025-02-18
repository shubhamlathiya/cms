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
    private EmailUtil emailUtil;

    @GetMapping("/dashboard")
    public String facultyDashboard(Model model) {
        return "FacultyManagement/faculty-dashboard";
    }

    // 1️⃣ Show all faculties
    @GetMapping
    public String getAllFaculties(Model model) {
        List<FacultyModel> faculties = facultyService.getAllFaculties();
        model.addAttribute("faculties", faculties);
        return "FacultyManagement/faculty/faculty-list";
    }


    // 2️⃣ Show add faculty form
    @GetMapping(PathConstant.ADD_PATH)
    public String showAddFacultyForm(Model model) {
        model.addAttribute("faculty", new FacultyModel());
        model.addAttribute("departments", departmentService.getAllDepartments()); // Add departments
        return "FacultyManagement/faculty/faculty-form"; // Returns faculty-form.html
    }

    // 3️⃣ Save new faculty
    @PostMapping("/register")
    public String registerFaculty(@ModelAttribute FacultyModel faculty,        // Faculty object
                                  @ModelAttribute UserModel user) {            // User object for username, password, and role

        try {
            // Accessing values directly from faculty and user objects
            String username = user.getUsername();
            String password = user.getPassword();
            String firstName = faculty.getFirstName();
            String lastName = faculty.getLastName();
            String designation = faculty.getDesignation();
            String qualification = faculty.getQualification();
            int experience = faculty.getExperience();
            String phoneNumber = faculty.getPhoneNumber();
            String email = faculty.getEmail();
            String status = faculty.getStatus();  // e.g., Active or Inactive

            DepartmentModel department = faculty.getDepartment(); // Get the department

            // Send a welcome email (or any other email logic you need)
            emailUtil.sendSimpleEmail(email, "Welcome to the Faculty System", "Hello " + designation + ", welcome to the faculty!");

            // Log all the values to check that they are correctly mapped
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Designation: " + designation);
            System.out.println("Qualification: " + qualification);
            System.out.println("Experience: " + experience);
            System.out.println("Phone Number: " + phoneNumber);
            System.out.println("Email: " + email);
            System.out.println("Status: " + status);
            System.out.println("Department: " + department.getName());

            // Call the service to register the faculty
            facultyService.registerFaculty(username, password, firstName, lastName, designation, qualification, experience, phoneNumber, email, department, status);

            // Redirect to success page
            return "redirect:/faculty";  // Success page after registration

        } catch (Exception ex) {
            // Log the error details and redirect to error page
            ex.printStackTrace();
            return "redirect:/faculty/error";  // Error page in case of failure
        }
    }


    // 4️⃣ Show update faculty form
    @GetMapping("/edit/{id}")
    public String showEditFacultyForm(@PathVariable Long id, Model model) {
        Optional<FacultyModel> faculty = facultyService.getFacultyById(id);
        if (faculty.isPresent()) {
            model.addAttribute("faculty", faculty.get());
            model.addAttribute("departments", departmentService.getAllDepartments()); // Load departments for selection
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
        return "FacultyManagement/faculty/faculty_subjects"; // Renders faculty-subjects.html
    }

}
