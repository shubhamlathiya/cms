package com.codershubham.cms.cms.controller.StudentManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SyllabusModel;
import com.codershubham.cms.cms.model.DTO.StudentQuestionsDto;
import com.codershubham.cms.cms.model.StudentManagementModules.*;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import com.codershubham.cms.cms.service.CourseManagementModules.SyllabusService;
import com.codershubham.cms.cms.service.FacultyManagementModules.AttendanceService;
import com.codershubham.cms.cms.service.StudentManagementModules.AssignmentService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(PathConstant.STUDENTS_PATH)
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SyllabusService syllabusService;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private HttpSession session;

    @GetMapping("/dashboard")
    public String studentDashboard(Model model) {
        Long userId = (Long) session.getAttribute("userId");

        StudentModel studentId = studentService.getStudentByUserId(userId);

        session.setAttribute("studentId", studentId.getId());
        // Fetch the student by ID
        StudentModel student = studentService.findById(studentId.getId());
        model.addAttribute("student", student);

        // Check if the student exists
        if (student == null) {
            // Handle the case where the student is not found (e.g., redirect to an error page)
            return "error/404"; // You can create a 404 error page
        }

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        // Add the student to the model
        model.addAttribute("userId", userId);
        // Return the view name
        return "StudentManagement/dashboard"; // Ensure this matches your Thymeleaf template name
    }

    @GetMapping("/attendance/{id}")
    public String viewAttendance(@PathVariable Long id, Model model) {
        // Fetch all enrollments for the student
        List<StudentEnrollmentModel> enrollments = studentService.getEnrollmentsByStudentId(id);

        // Map to store subject-wise attendance percentages per semester
        Map<Long, Map<SubjectsModel, Double>> attendancePercentageBySemester = new HashMap<>();

        for (StudentEnrollmentModel enrollment : enrollments) {
            SemesterModel semester = enrollment.getSemester();
            DivisionModel division = enrollment.getDivision();

            // Ensure semester and division exist
            if (semester == null || division == null) {
                continue; // Skip invalid enrollments
            }

            Long semesterId = semester.getId();
            Long divisionId = division.getId(); // Fetch division ID

            // Get all subjects the student is enrolled in for this semester
            List<SubjectEnrollmentModel> subjects = studentService.getSubjectsByStudentIdAndSemester(id, semesterId);

            // Map to store subject-wise attendance percentage
            Map<SubjectsModel, Double> subjectAttendanceMap = new HashMap<>();

            for (SubjectEnrollmentModel subjectEnrollment : subjects) {
                SubjectsModel subject = subjectEnrollment.getSubject();

                if (subject == null) {
                    continue; // Skip invalid subjects
                }

                Long subjectId = subject.getSubjectid();

                // Fetch attendance data
                int attendedLectures = attendanceService.countPresentLectures(id, subjectId);
                int totalLectures = attendanceService.getTotalLecturesBySubjectAndDivision(divisionId, subjectId);

                // Calculate attendance percentage safely
                double attendancePercentage = (totalLectures > 0) ? ((double) attendedLectures / totalLectures) * 100.0 : 0.0;

                // Store attendance percentage
                subjectAttendanceMap.put(subject, attendancePercentage);
            }

            // Store subject attendance data per semester
            attendancePercentageBySemester.put(semesterId, subjectAttendanceMap);
        }

        // Add the data to the model for Thymeleaf rendering
        model.addAttribute("studentId", id);
        model.addAttribute("attendancePercentageBySemester", attendancePercentageBySemester);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        Long studentId = (Long) session.getAttribute("studentId");
        StudentModel student = studentService.findById(studentId);
        model.addAttribute("student", student);
        return "StudentManagement/attendance-view"; // Return view name
    }


    @GetMapping("/lesson-plan/{id}")
    public String viewLessonPlan(@PathVariable Long id, Model model) {
        // Fetch all semesters for the student
        List<StudentEnrollmentModel> enrollments = studentService.getEnrollmentsByStudentId(id);

        // Map to store subjects and syllabus list per semester
        Map<Long, Map<SubjectsModel, List<SyllabusModel>>> subjectsBySemester = new HashMap<>();

        for (StudentEnrollmentModel enrollment : enrollments) {
            SemesterModel semester = enrollment.getSemester();
            if (semester == null) continue; // Skip if semester is null

            Long semesterId = semester.getId();
            List<SubjectEnrollmentModel> subjects = studentService.getSubjectsByStudentIdAndSemester(id, semesterId);

            // Map to store syllabus list per subject
            Map<SubjectsModel, List<SyllabusModel>> subjectSyllabusMap = new HashMap<>();

            for (SubjectEnrollmentModel subjectEnrollment : subjects) {
                SubjectsModel subject = subjectEnrollment.getSubject();
                if (subject == null) continue; // Skip if subject is null

                // Fetch syllabus list for the subject
                List<SyllabusModel> syllabusList = syllabusService.getSyllabusBySubject(subject.getSubjectid());

                // Store subject with syllabus list
                subjectSyllabusMap.put(subject, syllabusList);
            }

            // Store subjects and syllabus under the corresponding semester
            subjectsBySemester.put(semesterId, subjectSyllabusMap);
        }

        model.addAttribute("studentId", id);
        model.addAttribute("subjectsBySemester", subjectsBySemester); // Pass subjects & syllabus

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        Long studentId = (Long) session.getAttribute("studentId");
        StudentModel student = studentService.findById(studentId);
        model.addAttribute("student", student);

        return "StudentManagement/lesson-plan"; // Return updated view
    }

    @GetMapping("/assignments/{id}")
    public String viewAssignments(@PathVariable Long id, Model model) {
        // Fetch all enrollments for the student
        List<StudentEnrollmentModel> enrollments = studentService.getEnrollmentsByStudentId(id);

        // Map to store subjects and their assignments per semester
        Map<Long, Map<SubjectsModel, Boolean>> subjectAssignmentMap = new HashMap<>();

        for (StudentEnrollmentModel enrollment : enrollments) {
            SemesterModel semester = enrollment.getSemester();
            DivisionModel division = enrollment.getDivision();

            if (semester == null || division == null) {
                continue; // Skip invalid enrollments
            }

            Long semesterId = semester.getId();
            Long divisionId = division.getId();

            // Get subjects enrolled by the student for this semester
            List<SubjectEnrollmentModel> subjects = studentService.getSubjectsByStudentIdAndSemester(id, semesterId);

            // Map to store whether an assignment exists for each subject
            Map<SubjectsModel, Boolean> subjectHasAssignmentMap = new HashMap<>();

            for (SubjectEnrollmentModel subjectEnrollment : subjects) {
                SubjectsModel subject = subjectEnrollment.getSubject();

                if (subject == null) {
                    continue; // Skip invalid subjects
                }

                Long subjectId = subject.getSubjectid();

                // Check if assignments exist for this subject and division
                boolean hasAssignments = assignmentService.hasAssignmentsForSubjectAndDivision(subjectId, divisionId);

                // Store assignment availability
                subjectHasAssignmentMap.put(subject, hasAssignments);
            }

            // Store subject assignments data per semester
            subjectAssignmentMap.put(semesterId, subjectHasAssignmentMap);
        }

        // Add the data to the model for Thymeleaf rendering
        model.addAttribute("studentId", id);
        model.addAttribute("subjectAssignmentMap", subjectAssignmentMap);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        Long studentId = (Long) session.getAttribute("studentId");
        StudentModel student = studentService.findById(studentId);
        model.addAttribute("student", student);

        return "StudentManagement/assignments/student-assignments-view"; // Return view name
    }

    @GetMapping("/assignments/{studentId}/{subjectId}/questions")
    public String viewAssignedQuestions(@PathVariable Long studentId, @PathVariable Long subjectId, Model model) {
        // Fetching the StudentAssignments for the student and subject
        List<StudentAssignmentModel> studentAssignments = assignmentService.getAssignmentsByStudentAndSubject(studentId, subjectId);

        // Grouping questions by student ID
        Map<Long, StudentQuestionsDto> groupedQuestions = new HashMap<>();

        Long assignmentId = studentAssignments.isEmpty() ? null : studentAssignments.get(0).getAssignment().getId();

        // Fetch if the student has already submitted the assignment
        boolean hasSubmitted = assignmentService.hasStudentSubmittedAssignment(assignmentId, studentId);

        // Iterate over the studentAssignments and group questions by student
        for (StudentAssignmentModel studentAssignment : studentAssignments) {
            Long studentIdFromAssignment = studentAssignment.getStudent().getId();
            String questionText = studentAssignment.getQuestion().getQuestionText();

            // If the student is not already in the map, add them
            groupedQuestions.putIfAbsent(studentIdFromAssignment, new StudentQuestionsDto(studentIdFromAssignment, studentAssignment.getStudent().getFirstName()));

            // Add the question to the student's list of questions
            groupedQuestions.get(studentIdFromAssignment).getQuestions().add(questionText);
        }

        // Convert the map values into a list to pass to the view
        List<StudentQuestionsDto> studentQuestionsList = new ArrayList<>(groupedQuestions.values());

        // Finding the maximum number of questions assigned to any student
        int maxQuestions = studentQuestionsList.stream().mapToInt(sa -> sa.getQuestions().size()).max().orElse(0);

        // Add attributes to the model for rendering in the view
        model.addAttribute("assignedQuestions", studentQuestionsList);
        model.addAttribute("maxQuestions", maxQuestions);
        model.addAttribute("studentId", studentId);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("assignmentId", assignmentId);

        // Pass the status of submission to the view
        model.addAttribute("hasSubmitted", hasSubmitted); // True or false depending on whether submission exists

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);


        // Fetch the student by ID
        StudentModel student = studentService.findById(studentId);
        model.addAttribute("student", student);

        return "StudentManagement/assignments/student-assigned-questions";
    }


    // Display departments, courses, and students
    @GetMapping("/select")
    public String showStudentSelectionPage(Model model) {
        // Fetch all departments
        List<DepartmentModel> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        return "StudentManagement/students/list-students";
    }

}
