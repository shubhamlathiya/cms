package com.codershubham.cms.cms.controller.StudentManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SyllabusModel;
import com.codershubham.cms.cms.model.DTO.AttendanceRecordDto;
import com.codershubham.cms.cms.model.DTO.StudentQuestionsDto;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizAnswerModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizAttemptModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizQuestionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.*;
import com.codershubham.cms.cms.repository.StudentManagementModules.SemesterRepository;
import com.codershubham.cms.cms.service.CourseManagementModules.SyllabusService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.ExamFormService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.ExamService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.QuizModule.QuizAttemptService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.QuizModule.QuizQuestionService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.QuizModule.QuizService;
import com.codershubham.cms.cms.service.FacultyManagementModules.AttendanceService;
import com.codershubham.cms.cms.service.StudentManagementModules.AssignmentService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizAttemptService quizAttemptService;

    @Autowired
    private QuizQuestionService quizQuestionService;

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

    @GetMapping("/attendance/{id}")
    public String viewAttendance(@PathVariable Long id, Model model) {
        // Fetch all enrollments for the student
        List<StudentEnrollmentModel> enrollments = studentService.getEnrollmentsByStudentId(id);

        // Map to store subject-wise attendance percentages per semester
        Map<Long, Map<SubjectsModel, Double>> attendancePercentageBySemester = new HashMap<>();

        Long divisionId1 = enrollments.get(0).getDivision().getId();
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
        model.addAttribute("divisionId", divisionId1);
        return "StudentManagement/attendance-view"; // Return view name
    }

    @GetMapping("/details/{studentId}/{divisionId}/{subjectId}")
    public ResponseEntity<List<AttendanceRecordDto>> getAttendanceDetails(
            @PathVariable Long studentId,
            @PathVariable Long divisionId,
            @PathVariable Long subjectId) {

        List<AttendanceRecordDto> attendanceRecords = attendanceService.getAttendanceByStudentAndSubject(studentId, divisionId, subjectId);

        if (attendanceRecords.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 No Content if no records are found
        }
        return ResponseEntity.ok(attendanceRecords); // Returns 200 OK with the attendance data
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

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

//        Long studentId = (Long) session.getAttribute("studentId");
//        StudentModel student = studentService.findById(studentId);
//        model.addAttribute("student", student);

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

    @GetMapping("/quiz/{id}")
    public String viewQuiz(@PathVariable Long id, Model model, HttpSession session) {
        // Fetch all enrollments for the student
        List<StudentEnrollmentModel> enrollments = studentService.getEnrollmentsByStudentId(id);

        // Map to store subjects and their quizzes per semester, along with the attempt status
        Map<Long, Map<SubjectsModel, List<Map<String, Object>>>> subjectQuizMap = new HashMap<>();

        Long studentId = (Long) session.getAttribute("studentId");

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

            // Map to store available quizzes for each subject
            Map<SubjectsModel, List<Map<String, Object>>> subjectQuizListMap = new HashMap<>();

            for (SubjectEnrollmentModel subjectEnrollment : subjects) {
                SubjectsModel subject = subjectEnrollment.getSubject();

                if (subject == null) {
                    continue; // Skip invalid subjects
                }

                Long subjectId = subject.getSubjectid();

                // Fetch quizzes for this subject and division
                List<QuizModel> quizzes = quizService.getQuizzesForSubjectAndDivision(subjectId, divisionId, semesterId);

                // List to store quizzes and attempt status for this subject
                List<Map<String, Object>> quizAttemptList = new ArrayList<>();

                for (QuizModel quiz : quizzes) {
                    // Check if student has already attempted the quiz
                    Optional<QuizAttemptModel> attempt = quizAttemptService.getAttemptByStudentAndQuiz(studentId, quiz.getId());

                    // Create a map to store the quiz and its attempt status
                    Map<String, Object> quizData = new HashMap<>();
                    quizData.put("quiz", quiz);
                    quizData.put("attempt", attempt.orElse(null)); // null if not attempted

                    // Add to the list
                    quizAttemptList.add(quizData);
                }

                // Store quizzes with attempt data for this subject
                subjectQuizListMap.put(subject, quizAttemptList);
            }

            // Store subject quizzes data per semester
            subjectQuizMap.put(semesterId, subjectQuizListMap);
        }

        // Add data to model for Thymeleaf rendering
        model.addAttribute("studentId", id);
        model.addAttribute("subjectQuizMap", subjectQuizMap);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        StudentModel student = studentService.findById(studentId);
        model.addAttribute("student", student);

        return "StudentManagement/quiz/quiz-view"; // Return view name
    }

    @GetMapping("/quiz/{quizId}/start")
    public String startQuiz(@PathVariable Long quizId, Model model) {
        Long studentId = (Long) session.getAttribute("studentId");

        // Fetch quiz and student details
        QuizModel quiz = quizService.getQuizById(quizId);
        StudentModel student = studentService.findById(studentId);

        // Check if the quiz has already been attempted
        Optional<QuizAttemptModel> existingAttempt = quizAttemptService.getAttemptByStudentAndQuiz(studentId, quizId);
        if (existingAttempt.isPresent()) {
            model.addAttribute("message", "You have already attempted this quiz.");
            return "StudentManagement/quiz/quiz-result"; // Redirect to result page if already attempted
        }

        // Show quiz details (questions and options)
        model.addAttribute("quiz", quiz);
        model.addAttribute("student", student);

        return "StudentManagement/quiz/quiz-environment"; // Return quiz page with questions
    }

    @PostMapping("/quiz/{quizId}/submit")
    public String submitQuiz(@PathVariable Long quizId, @RequestParam Map<String, String> responses, HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("studentId");

        // Fetch quiz and student details
        QuizModel quiz = quizService.getQuizById(quizId);
        StudentModel student = studentService.findById(studentId);

        // Check if quiz is already attempted by this student
        Optional<QuizAttemptModel> existingAttempt = quizAttemptService.getAttemptByStudentAndQuiz(studentId, quizId);
        if (existingAttempt.isPresent()) {
            model.addAttribute("message", "You have already attempted this quiz.");
            return "StudentManagement/quiz/quiz-result"; // Redirect to result page if already attempted
        }

        // Create a new quiz attempt record
        QuizAttemptModel attempt = new QuizAttemptModel();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);

        int totalScore = 0;
        List<QuizAnswerModel> answerList = new ArrayList<>();

        // Iterate over each question in the quiz
        for (QuizQuestionModel question : quiz.getQuestions()) {
            String paramName = "question_" + question.getId();
            String selectedOptionStr = responses.get(paramName);

            // Check if an answer was provided for this question
            if (selectedOptionStr != null) {
                int selectedOption = Integer.parseInt(selectedOptionStr);
                boolean isCorrect = (selectedOption == question.getCorrectOption());

                // Increase the score if the answer is correct
                if (isCorrect) {
                    totalScore += 1;
                }

                // Create a new answer object to store the selected option and its correctness
                QuizAnswerModel answer = new QuizAnswerModel();
                answer.setAttempt(attempt);
                answer.setQuestion(question);
                answer.setSelectedOption(selectedOption);
                answer.setCorrect(isCorrect);
                answerList.add(answer);
            }
        }

        // Set final score for the quiz attempt
        attempt.setScore(totalScore);

        // Save the quiz attempt and associated answers
        quizAttemptService.saveAttempt(attempt, answerList);

        // Add attributes for the result page
        model.addAttribute("quiz", quiz);
        model.addAttribute("attempt", attempt);
        model.addAttribute("message", "Quiz Submitted Successfully!");

        return "StudentManagement/quiz/quiz-result"; // Redirect to result page
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
