package com.codershubham.cms.cms.controller.UserManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.util.EmailUtil;
import com.codershubham.cms.cms.util.PdfGeneratorUtil;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(PathConstant.ADMIN_PATH)
public class AdminController {

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private HttpSession session;

    @Autowired
    private FacultyService facultyService;

    @GetMapping(PathConstant.DASHBOARD_PATH)
    public String dashboard(Model model) {

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "UserManagement/admin/dashboard";
    }

    // Display departments, courses, and students
    @GetMapping("/students")
    public String showStudentSelectionPage(Model model) {
        // Fetch all departments
        List<DepartmentModel> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/students/list-students";
    }

    @GetMapping("/students/download")
    public ResponseEntity<byte[]> downloadPdf(@RequestParam("divisionId") Long divisionId) {
        // Fetch the student data based on divisionId (replace with your actual service logic)
        List<StudentModel> students = studentService.getStudentsByDivision(divisionId);

        // Prepare the data for the table in the PDF
        List<String[]> tableData = new ArrayList<>();
        tableData.add(new String[]{"ID", "First Name", "Last Name", "Email"});

        for (StudentModel student : students) {
            tableData.add(new String[]{
                    String.valueOf(student.getId()),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail()
            });
        }

        // Generate the PDF with the student list table
        byte[] pdfBytes = PdfGeneratorUtil.generatePdfWithTable("Student List", tableData);

        // Return the PDF as a download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_list.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // Show the student registration form
    @GetMapping("/students/add")
    public String showAddStudentPage(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/students/student-register";
    }

    @GetMapping("/students/subject-enrollment")
    public String showStudentSubjectEnrollment(Model model) {
        // Fetch all departments
        List<DepartmentModel> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/students/student-subject-enrollment";
    }

    // Fetch courses based on selected department
    @GetMapping("/students/courses/{departmentId}")
    @ResponseBody
    public List<CourseModel> getCoursesByDepartment(@PathVariable Long departmentId) {
        return courseService.getCoursesByDepartmentId(departmentId);
    }

    @GetMapping("/students/semesters/{courseId}")
    public ResponseEntity<List<SemesterModel>> getSemestersByCourse(@PathVariable Long courseId) {
        List<SemesterModel> semesters = semesterService.getSemestersByCourse(courseId);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/students/divisions/{semesterId}")
    public ResponseEntity<List<DivisionModel>> getDivisionsBySemester(@PathVariable Long semesterId) {
        List<DivisionModel> divisions = divisionService.getDivisionsBySemester(semesterId);
        return ResponseEntity.ok(divisions);
    }

    @GetMapping("/students/subjects/{courseId}")
    public ResponseEntity<List<SubjectsModel>> getSubjectsByCourse(@PathVariable Long courseId) {
        List<SubjectsModel> subjects = subjectService.getSubjectsByCourseId(courseId);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/students/list/{divisionId}")
    public ResponseEntity<List<StudentModel>> getStudentsByDivision(@PathVariable Long divisionId) {
        List<StudentModel> students = studentService.getStudentsByDivision(divisionId);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/students/register")
    public String registerStudent(@ModelAttribute StudentModel student, @ModelAttribute UserModel user) {

        try {
            // Accessing values directly from student and user objects
            String username = user.getUsername();
            String password = user.getPassword();
            String firstName = student.getFirstName();
            String lastName = student.getLastName();
            String email = student.getEmail();
            CourseModel course = student.getCourse();
            String phoneNumber = student.getPhoneNumber();
            String address = student.getAddress();

            // Construct email subject and body
            String subject = "Welcome to Our System, " + firstName + "!";

            String body = "Dear " + firstName + " " + lastName + ",\n\n" + "Welcome to our system! We are excited to have you as a part of our institution. Below are your account details:\n\n" + "Username: " + username + "\n" + "Password: " + password + "\n\n" + "Course Enrolled: " + course.getCourseName() + "\n" + "Registered Phone Number: " + phoneNumber + "\n" + "Registered Address: " + address + "\n\n" + "Please keep these credentials safe and do not share them with anyone. You can now log in to the student portal to explore your courses, schedules, and other resources.\n\n" + "If you have any questions or need assistance, feel free to reach out.\n\n" + "Best Regards,\n" + "Your Institution's Name\n" + "Your Support Team\n" + "Your Contact Information\n" + "Your Website (if applicable)";

// Send the email
            emailUtil.sendSimpleEmail(email, subject, body);

// Log values for verification
            System.out.println("Welcome email sent to: " + email);
            System.out.println(username);
            System.out.println(password);
            System.out.println(firstName);
            System.out.println(lastName);
            System.out.println(email);
            System.out.println(phoneNumber);
            System.out.println(address);
            System.out.println("Course ID: " + course.getCourseID());
            System.out.println("Course Name: " + course.getCourseName());

            // Call the service to register the student
            studentService.registerStudent(username, password, firstName, lastName, email, phoneNumber, address, course);

            // Redirect to success page
            return "redirect:/admin/students/success";

        } catch (Exception ex) {
            // Log the error details and redirect to error page
            ex.printStackTrace();
            return "redirect:/students/error";
        }
    }

    @GetMapping("/students/success")
    public String registrationSuccess(Model model) {

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/students/registration-success";
    }

    @PostMapping("/students/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid Excel file.");
        }
        studentService.saveStudentsFromExcel(file);
        return ResponseEntity.ok("Students successfully uploaded!");
    }

    // 1️⃣ Show all faculties
    @GetMapping("/faculty")
    public String getAllFaculties(Model model) {
        List<FacultyModel> faculties = facultyService.getAllFaculties();
        model.addAttribute("faculties", faculties);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "FacultyManagement/faculty/faculty-list";
    }

    // 2️⃣ Show add faculty form
    @GetMapping("/faculty/add")
    public String showAddFacultyForm(Model model) {
        model.addAttribute("faculty", new FacultyModel());
        model.addAttribute("departments", departmentService.getAllDepartments()); // Add departments

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "FacultyManagement/faculty/faculty-form"; // Returns faculty-form.html
    }

    @PostMapping("/faculty/register")
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

            // Prepare email content
            String subject = "Welcome to the Faculty System!";
            String body = "Dear " + designation + " " + lastName + ",\n\n" + "We are delighted to welcome you to the faculty team! We are confident that your expertise and experience will contribute significantly to our institution.\n\n" + "Here are your registered details:\n\n" + "Username: " + username + "\n" + "Designation: " + designation + "\n" + "Qualification: " + qualification + "\n" + "Experience: " + experience + " years\n" + "Department: " + department.getName() + "\n" + "Phone Number: " + phoneNumber + "\n" + "Email: " + email + "\n" + "Status: " + status + "\n\n" + "You can now access the faculty portal and start exploring your assigned courses, schedules, and other faculty resources. " + "If you have any questions or need assistance, feel free to reach out.\n\n" + "We look forward to working with you and wish you a successful journey ahead!\n\n" + "Best regards,\n" + "Your Institution's Name\n" + "Your Contact Information\n" + "Your Website (if applicable)";

// Send the email
            emailUtil.sendSimpleEmail(email, subject, body);

// Log values for verification
            System.out.println("Welcome email sent to: " + email);
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
            return "redirect:/admin/faculty/success";  // Success page after registration

        } catch (Exception ex) {
            // Log the error details and redirect to error page
            ex.printStackTrace();
            return "redirect:/faculty/error";  // Error page in case of failure
        }
    }

    @GetMapping("/faculty/success")
    public String facultyRegistrationSuccess(Model model) {

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/students/registration-success";
    }

    // 4️⃣ Show update faculty form
    @GetMapping("/faculty/edit/{id}")
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
    @PostMapping("/faculty/update/{id}")
    public String updateFaculty(@PathVariable Long id, @ModelAttribute FacultyModel faculty) {
        facultyService.updateFaculty(id, faculty);
        return "redirect:/faculty";
    }

    // 6️⃣ Delete faculty
    @GetMapping("/faculty/delete/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return "redirect:/faculty"; // Redirect after deleting
    }

    @GetMapping("/faculty-subject/assign-subject")
    public String showAssignmentPage(Model model) {
        model.addAttribute("faculties", facultyService.getAllFaculties());
        model.addAttribute("departments", departmentService.getAllDepartments());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "FacultyManagement/faculty/assign-faculty-subjects"; // Loads faculty_assignment.html
    }
}
