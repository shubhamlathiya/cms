package com.codershubham.cms.cms.controller.StudentManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.AttendanceModel;
import com.codershubham.cms.cms.model.StudentManagementModules.*;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import com.codershubham.cms.cms.service.FacultyManagementModules.AttendanceService;
import com.codershubham.cms.cms.service.StudentManagementModules.AssignmentService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(PathConstant.STUDENTS_PATH)
public class StudentController {

    @Autowired
    private StudentService studentService;

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
    private EmailUtil emailUtil;

    // Show the student registration form
    @GetMapping("/add")
    public String showAddStudentPage(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "StudentManagement/students/student_register";
    }

    // Display departments, courses, and students
    @GetMapping("/select")
    public String showStudentSelectionPage(Model model) {
        // Fetch all departments
        List<DepartmentModel> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        return "StudentManagement/students/list-students";
    }

    // Fetch courses based on selected department
    @GetMapping("/courses/{departmentId}")
    @ResponseBody
    public List<CourseModel> getCoursesByDepartment(@PathVariable Long departmentId) {
        return courseService.getCoursesByDepartmentId(departmentId);
    }

    @GetMapping("/subject-enrollment")
    public String showStudentSubjectEnrollment(Model model) {
        // Fetch all departments
        List<DepartmentModel> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        return "StudentManagement/students/student_subject_enrollment";
    }

    @PostMapping("/register")
    public String registerStudent(@ModelAttribute StudentModel student, @ModelAttribute UserModel user) {

        try {
            // Accessing values directly from student and user objects
            String username = user.getUsername();
            String password = user.getPassword();
            String firstName = student.getFirstName();
            String lastName = student.getLastName();
            String email = student.getEmail();

            // Construct email subject and body
            String subject = "Welcome to Our System, " + firstName + "!";
            String body = "Dear " + firstName + " " + lastName + ",\n\n" + "Welcome to our system. Here are your account details:\n\n" + "Username: " + username + "\n" + "Password: " + password + "\n\n" + "Please keep these credentials safe and do not share them with anyone.\n\n" + "Best Regards,\nYour Team";

            // Send email
            emailUtil.sendSimpleEmail(email, subject, body);

            String phoneNumber = student.getPhoneNumber();
            String address = student.getAddress();
            // Fetch the courseId from the request
            CourseModel course = student.getCourse();  // Ensure getCourse() returns a Course object

            // Log all the values to check that they are correctly mapped
            System.out.println(username);
            System.out.println(password);
            System.out.println(firstName);
            System.out.println(lastName);
            System.out.println(email);
            System.out.println(phoneNumber);
            System.out.println(address);
            System.out.println("Course ID: " + course.getCourseID());

            // Call the service to register the student
            studentService.registerStudent(username, password, firstName, lastName, email, phoneNumber, address, course);

            // Redirect to success page
            return "redirect:/students/success";

        } catch (Exception ex) {
            // Log the error details and redirect to error page
            ex.printStackTrace();
            return "redirect:/students/error";
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid Excel file.");
        }
        studentService.saveStudentsFromExcel(file);
        return ResponseEntity.ok("Students successfully uploaded!");
    }


    @GetMapping("/semesters/{courseId}")
    public ResponseEntity<List<SemesterModel>> getSemestersByCourse(@PathVariable Long courseId) {
        List<SemesterModel> semesters = semesterService.getSemestersByCourse(courseId);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/divisions/{semesterId}")
    public ResponseEntity<List<DivisionModel>> getDivisionsBySemester(@PathVariable Long semesterId) {
        List<DivisionModel> divisions = divisionService.getDivisionsBySemester(semesterId);
        return ResponseEntity.ok(divisions);
    }

    @GetMapping("/subjects/{courseId}")
    public ResponseEntity<List<SubjectsModel>> getSubjectsByCourse(@PathVariable Long courseId) {
        List<SubjectsModel> subjects = subjectService.getSubjectsByCourseId(courseId);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/list/{divisionId}")
    public ResponseEntity<List<StudentModel>> getStudentsByDivision(@PathVariable Long divisionId) {
        List<StudentModel> students = studentService.getStudentsByDivision(divisionId);
        return ResponseEntity.ok(students);
    }

    // Success page
    @GetMapping("/success")
    public String registrationSuccess() {
        return "StudentManagement/students/registration_success";
    }
}
