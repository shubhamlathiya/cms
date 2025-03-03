package com.codershubham.cms.cms.controller.StudentManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.DTO.StudentQuestionsDto;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.StudentManagementModules.AssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.AssignmentSubmissionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentAssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.StudentManagementModules.AssignmentService;
import com.codershubham.cms.cms.util.PdfGeneratorUtil;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping(PathConstant.ASSIGNMENTS_PATH)
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private HttpSession session;

    @GetMapping("/{divisionId}/{semesterId}/{subjectId}")
    public String getAssignments(@PathVariable Long divisionId, @PathVariable Long semesterId, @PathVariable Long subjectId, Model model) {
        List<AssignmentModel> assignments = assignmentService.getAssignmentsByDivisionSemesterAndSubject(divisionId, semesterId, subjectId);
        model.addAttribute("assignments", assignments);
        model.addAttribute("divisionId", divisionId);
        model.addAttribute("semesterId", semesterId);
        model.addAttribute("subjectId", subjectId);

        Long userId = (Long) session.getAttribute("userId");

        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        model.addAttribute("faculty", faculty);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        return "StudentManagement/assignments/assignments";
    }

    @GetMapping("/{divisionId}/{semesterId}/{subjectId}/new")
    public String showAddAssignmentForm(@PathVariable Long divisionId, @PathVariable Long semesterId, @PathVariable Long subjectId, Model model) {
        model.addAttribute("divisionId", divisionId);
        model.addAttribute("semesterId", semesterId);
        model.addAttribute("subjectId", subjectId);

        Long userId = (Long) session.getAttribute("userId");

        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        model.addAttribute("faculty", faculty);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        return "StudentManagement/assignments/add-assignment";
    }

    @PostMapping("/{divisionId}/{semesterId}/{subjectId}/save")
    public String saveAssignment(@PathVariable Long divisionId, @PathVariable Long semesterId, @PathVariable Long subjectId, @RequestParam String assignmentName, @RequestParam LocalDateTime deadline, @RequestParam int maxMarks, @RequestParam(required = false) MultipartFile material, @RequestParam(required = false) Integer maxQuestions, @RequestParam String questions, @RequestParam(required = false) boolean randomAssignment, @RequestParam(required = false) MultipartFile file, Model model) {

        if (questions == null || questions.isEmpty()) {
            model.addAttribute("error", "Please provide questions.");
            return "StudentManagement/assignments/add-assignment";
        }

        assignmentService.createAssignment(divisionId, semesterId, subjectId, assignmentName, deadline, maxMarks, material, maxQuestions, questions, randomAssignment, file);

        return "redirect:/assignments/" + divisionId + "/" + semesterId + "/" + subjectId;
    }

    @GetMapping("/{assignmentId}/assigned-questions")
    public String getAssignedQuestions(@PathVariable Long assignmentId, Model model) {

        // Fetching assigned questions from the service
        List<StudentAssignmentModel> assignedQuestions = assignmentService.getAssignedQuestions(assignmentId);

        // Grouping questions by student ID
        Map<Long, StudentQuestionsDto> groupedQuestions = new HashMap<>();

        // Iterate through each assigned question
        for (StudentAssignmentModel assignment : assignedQuestions) {
            Long studentId = assignment.getStudent().getId();
            String questionText = assignment.getQuestion().getQuestionText();

            // If student is not already in the map, add them
            groupedQuestions.putIfAbsent(studentId, new StudentQuestionsDto(studentId, assignment.getStudent().getFirstName()));

            // Add the question to the student's list of questions
            groupedQuestions.get(studentId).getQuestions().add(questionText);
        }

        // Convert the map values into a list to pass to the view
        List<StudentQuestionsDto> studentQuestionsList = new ArrayList<>(groupedQuestions.values());

        // Check for assignment submissions for each student and add submission info to the model
        for (StudentQuestionsDto studentQuestions : studentQuestionsList) {
            Long studentId = studentQuestions.getStudentId();
            AssignmentSubmissionModel submission = assignmentService.getAssignmentSubmission(assignmentId, studentId);

            // Check if submission exists and add file path
            if (submission != null && submission.getSubmissionFilePath() != null) {
                // If a submission exists and has a file path, add file path to the DTO
                studentQuestions.setFilePath(submission.getSubmissionFilePath());
                studentQuestions.setSubmitted(true); // Mark as submitted
            } else {
                // If no submission or no file path, set filePath to null and mark as not submitted
                studentQuestions.setFilePath(null);
                studentQuestions.setSubmitted(false);
            }
        }

        // Finding the maximum number of questions assigned to any student
        int maxQuestions = studentQuestionsList.stream().mapToInt(sa -> sa.getQuestions().size()).max().orElse(0);

        // Add attributes to the model for rendering in the view
        model.addAttribute("assignedQuestions", studentQuestionsList);
        model.addAttribute("maxQuestions", maxQuestions);

        Long userId = (Long) session.getAttribute("userId");

        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        model.addAttribute("faculty", faculty);
        model.addAttribute("assignmentId", assignmentId);
        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/assignments/assigned-questions";
    }

    @GetMapping("/{assignmentId}/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long assignmentId) {
        // Fetching assigned questions for the given assignment
        List<StudentAssignmentModel> assignedQuestions = assignmentService.getAssignedQuestions(assignmentId);

        // Grouping questions by student ID
        Map<Long, StudentQuestionsDto> groupedQuestions = new HashMap<>();
        for (StudentAssignmentModel assignment : assignedQuestions) {
            Long studentId = assignment.getStudent().getId();
            String questionText = assignment.getQuestion().getQuestionText();

            groupedQuestions.putIfAbsent(studentId, new StudentQuestionsDto(studentId, assignment.getStudent().getFirstName()));
            groupedQuestions.get(studentId).getQuestions().add(questionText);
        }

        // Convert map values into a list
        List<StudentQuestionsDto> studentQuestionsList = new ArrayList<>(groupedQuestions.values());

        // Check if students have submitted the assignment
        for (StudentQuestionsDto studentQuestions : studentQuestionsList) {
            Long studentId = studentQuestions.getStudentId();
            AssignmentSubmissionModel submission = assignmentService.getAssignmentSubmission(assignmentId, studentId);

            if (submission != null && submission.getSubmissionFilePath() != null) {
                studentQuestions.setFilePath(submission.getSubmissionFilePath());
                studentQuestions.setSubmitted(true);
            } else {
                studentQuestions.setFilePath(null);
                studentQuestions.setSubmitted(false);
            }
        }

        // Prepare data for the PDF table
        List<String[]> tableData = new ArrayList<>();
        tableData.add(new String[]{"Student ID", "Name", "Questions", "Submitted"}); // Header row

        for (StudentQuestionsDto student : studentQuestionsList) {
            String questions = String.join("; ", student.getQuestions()); // Combine questions in a readable format
            String submitted = student.isSubmitted() ? "Yes" : "No";
            tableData.add(new String[]{
                    String.valueOf(student.getStudentId()),
                    student.getStudentName(),
                    questions,
                    submitted
            });
        }

        // Generate PDF
        byte[] pdfBytes = PdfGeneratorUtil.generatePdfWithTable("Assignment #" + assignmentId, tableData);

        // Return the PDF as a download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=assigned_questions.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @PostMapping("/submit/{assignmentId}/{studentId}")
    public String submitAssignment(@PathVariable Long assignmentId, @PathVariable Long studentId, @RequestParam(required = false) MultipartFile submissionFile, @RequestParam(required = false) String remarks, Model model, RedirectAttributes redirectAttributes) throws IOException {
        try {
            String filePath = null;
            String uploadDir = "src/main/resources/static/uploads/assignments";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            if (!submissionFile.isEmpty()) {
                String originalFileName = submissionFile.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID() + fileExtension;
                Path path = Paths.get(uploadDir, uniqueFileName);
                Files.write(path, submissionFile.getBytes());

                filePath = "/uploads/assignments/" + uniqueFileName;
            }

            assignmentService.saveAssignmentSubmission(assignmentId, studentId, filePath, 0, remarks);

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Failed to save event: " + e.getMessage());
        }
        // Redirect to the assignments view page
        return "redirect:/students/assignments/" + studentId;
    }
}