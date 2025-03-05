package com.codershubham.cms.cms.controller.StudentManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SyllabusModel;
import com.codershubham.cms.cms.model.DTO.StudentQuestionsDto;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormStatus;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamModel;
import com.codershubham.cms.cms.model.StudentManagementModules.*;
import com.codershubham.cms.cms.repository.StudentManagementModules.SemesterRepository;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import com.codershubham.cms.cms.service.CourseManagementModules.SyllabusService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.ExamFormService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.ExamService;
import com.codershubham.cms.cms.service.FacultyManagementModules.AttendanceService;
import com.codershubham.cms.cms.service.StudentManagementModules.AssignmentService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping(PathConstant.STUDENTS_PATH)
public class StudentController {

    @Autowired
    private StudentService studentService;

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

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ExamFormService examFormService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private ExamService examService;

    @GetMapping(PathConstant.DASHBOARD_PATH)
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
        model.addAttribute("userId", userId);
        // Return the view name
        return "StudentManagement/dashboard"; // Ensure this matches your Thymeleaf template name
    }

//    @GetMapping("/profile")
//    public String studentProfile(Model model) {
//        Long userId = (Long) session.getAttribute("userId");
//
//        String userRole = userRoleUtil.getUserRole(session);
//        model.addAttribute("userRole", userRole);
//        model.addAttribute("userId", userId);
//
//        return "StudentManagement/profile";
//    }

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

    @GetMapping("/exam")
    public String viewExamForm(Model model) {
        // Fetch student details
        Long studentId = (Long) session.getAttribute("studentId");
        StudentModel student = studentService.findById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        // Check if the student has already submitted the exam form
        boolean hasSubmittedExamForm = examService.hasStudentSubmittedExamForm(studentId);
//    if (hasSubmittedExamForm) {
//        hasSubmittedExamForm = true;
//        model.addAttribute("message", "You have already submitted your exam form.");
//    }

        // Fetch the latest exam form based on the student's course and semester
        List<StudentEnrollmentModel> enrollments = studentService.getEnrollmentsByStudentId(studentId);
        Long semesterId = enrollments.isEmpty() ? null : enrollments.get(0).getSemester().getId();

        SemesterModel semester = semesterRepository.findById(semesterId).orElseThrow(() -> new RuntimeException("Course not found"));
        Long courseId = semester.getCourse().getCourseID();

        ExamFormModel examForm = examFormService.getExamFormByCourseAndSemester(courseId, semesterId);
        if (examForm == null) {
            throw new RuntimeException("No exam form found for the selected course and semester");
        }

        // Check eligibility based on exam form deadlines
        Date now = new Date();
        Date startDate = examForm.getStartDate();
        Date endDate = examForm.getEndDate();
        Date endDateWithLateFee = examForm.getEndDateWithLateFee();
        Date endDateWithSuperLateFee = examForm.getEndDateWithSuperLateFee();

        boolean isEligibleForExam = false;
        String eligibilityMessage = "Not eligible";

        if (now.before(endDate)) {
            isEligibleForExam = true;
            eligibilityMessage = "Eligible to fill the exam form on time";
        } else if (now.before(endDateWithLateFee)) {
            eligibilityMessage = "Eligible to fill the exam form with late fee";
        } else if (now.before(endDateWithSuperLateFee)) {
            eligibilityMessage = "Eligible to fill the exam form with super late fee";
        }

        // Add attributes to the model
        model.addAttribute("student", student);
        model.addAttribute("examForm", examForm);
        model.addAttribute("isEligibleForExam", isEligibleForExam);
        model.addAttribute("eligibilityMessage", eligibilityMessage);
        model.addAttribute("examFromFileUP", hasSubmittedExamForm);
        return "ExaminationManagement/exam-details-students";
    }

    @GetMapping("/exam/{id}")
    public String viewExamForm(@PathVariable Long id, Model model) {
        // Fetch all semesters for the student
        List<StudentEnrollmentModel> enrollments = studentService.getEnrollmentsByStudentId(id);

        // Map to store subjects per semester
        Map<Long, List<SubjectsModel>> subjectsBySemester = new HashMap<>();

        for (StudentEnrollmentModel enrollment : enrollments) {
            SemesterModel semester = enrollment.getSemester();
            if (semester == null) continue; // Skip if semester is null

            Long semesterId = semester.getId();
            List<SubjectEnrollmentModel> subjectEnrollments = studentService.getSubjectsByStudentIdAndSemester(id, semesterId);

            // Extract subjects from subject enrollments
            List<SubjectsModel> subjects = subjectEnrollments.stream().map(SubjectEnrollmentModel::getSubject).filter(Objects::nonNull) // Ensure no null subjects are added
                    .collect(Collectors.toList());
            System.out.println(subjects);
            // Store subjects under the corresponding semester
            subjectsBySemester.put(semesterId, subjects);
        }

        // Fetch the latest exam form for the student based on course_id and semester_id
//        Long courseId = enrollments.isEmpty() ? null : enrollments.getCourse().getId();
        Long semesterId = enrollments.isEmpty() ? null : enrollments.get(0).getSemester().getId();

        Long divisionID = enrollments.isEmpty() ? null : enrollments.get(0).getDivision().getId();

        DivisionModel division = divisionService.getDivisionById(divisionID);

        SemesterModel semester = semesterRepository.findById(semesterId).orElseThrow(() -> new RuntimeException("Course not found"));

        Long courseId = semester.getCourse().getCourseID();

        if (courseId != null && semesterId != null) {
            ExamFormModel examForm = examFormService.getExamFormByCourseAndSemester(courseId, semesterId);
            if (examForm != null) {
                // Initialize current date
                Date now = new Date();

                // Example deadlines from exam form (replace with actual values from your examForm object)
                Date startDate = new Date();  // Example start date
                Date endDate = new Date(System.currentTimeMillis() + 1000000000); // 1 hour from now (example)
                Date endDateWithLateFee = new Date(System.currentTimeMillis() + 2000000000); // 2 hours from now (example)
                Date endDateWithSuperLateFee = new Date(System.currentTimeMillis() + 300000000); // 3 hours from now (example)

                boolean isEligibleForExam = false;
                String eligibilityMessage = "Not eligible";

                // Check eligibility based on current date and deadlines
                if (now.before(endDate)) {
                    isEligibleForExam = true;
                    eligibilityMessage = "Eligible to fill the exam form on time";
                } else if (now.before(endDateWithLateFee)) {
                    eligibilityMessage = "Eligible to fill the exam form with late fee";
                } else if (now.before(endDateWithSuperLateFee)) {
                    eligibilityMessage = "Eligible to fill the exam form with super late fee";
                }


                // Add the exam eligibility information to the model
                model.addAttribute("examForm", examForm);
                model.addAttribute("isEligibleForExam", isEligibleForExam);
                model.addAttribute("eligibilityMessage", eligibilityMessage);
            }
        }

        model.addAttribute("studentId", id);
        model.addAttribute("subjectsBySemester", subjectsBySemester); // Pass subjects

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        Long studentId = (Long) session.getAttribute("studentId");
        StudentModel student = studentService.findById(studentId);
        model.addAttribute("student", student);
        model.addAttribute("division", division);

        return "ExaminationManagement/exam-form-students";
    }

    @PostMapping("/exam/submit")
    public String submitExamForm(@RequestParam Long studentId, @RequestParam Long facultyId, @RequestParam Long examFormId, @RequestParam List<Long> subjectIds, @RequestParam(required = false) Double feeAmount, RedirectAttributes redirectAttributes) {

        feeAmount = 1500.00;
        // Call service to handle the exam form submission
        boolean isSubmitted = examService.submitExamForm(studentId, facultyId, examFormId, subjectIds, feeAmount);

        if (isSubmitted) {
            redirectAttributes.addFlashAttribute("message", "Exam Form Submitted Successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Error occurred while submitting the exam form.");
        }

        return "redirect:/student/dashboard";
    }

//    @PostMapping("/exam/submit")
//    public String submitExamForm(
//            @RequestParam Long studentId,
//            @RequestParam Long examFormId,
//            @RequestParam List<Long> subjectIds,
//            @RequestParam Double feeAmount,
//            RedirectAttributes redirectAttributes) {
//
//        // Fetch student and exam form details
//        StudentModel student = studentService.findById(studentId);
//        ExamFormModel examForm = examFormService.findById(examFormId);
//        List<SubjectsModel> subjects = subjectService.findAllById(subjectIds);
//
//        // Create ExamFormDetails entry
//        ExamModel examFormDetails = new ExamModel();
//        examFormDetails.setStudent(student);
//        examFormDetails.setExamForm(examForm);
//        examFormDetails.setSubjects(subjects);
//        examFormDetails.setFeeAmount(feeAmount);
//        examFormDetails.setSubmissionDate(LocalDate.now());
//        examFormDetails.setStatus(ExamFormStatus.PENDING_APPROVAL);
//        examFormDetails.setPaymentStatus(false); // Payment not yet confirmed
//
//        // Save the exam form submission
//        exam.save(examFormDetails);
//
//        redirectAttributes.addFlashAttribute("message", "Exam Form Submitted Successfully!");
//        return "redirect:/student/dashboard";
//    }


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

}
