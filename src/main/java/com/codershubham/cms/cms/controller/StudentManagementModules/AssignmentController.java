package com.codershubham.cms.cms.controller.StudentManagementModules;

import com.codershubham.cms.cms.model.DTO.StudentQuestionsDTO;
import com.codershubham.cms.cms.model.StudentManagementModules.AssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentAssignmentModel;
import com.codershubham.cms.cms.service.StudentManagementModules.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // Step 1: Fetch all assignments for a division and semester
    @GetMapping("/{divisionId}/{semesterId}")
    public String getAssignments(@PathVariable Long divisionId, @PathVariable Long semesterId, Model model) {
        List<AssignmentModel> assignments = assignmentService.getAssignmentsByDivisionAndSemester(divisionId, semesterId);
        model.addAttribute("assignments", assignments);
        model.addAttribute("divisionId", divisionId);
        model.addAttribute("semesterId", semesterId);
        return "StudentManagement/assignments/assignments"; // HTML page to display assignments
    }

    // Step 2: Show the form to add a new assignment
    @GetMapping("/{divisionId}/{semesterId}/new")
    public String showAddAssignmentForm(@PathVariable Long divisionId, @PathVariable Long semesterId, Model model) {
        model.addAttribute("divisionId", divisionId);
        model.addAttribute("semesterId", semesterId);
        return "StudentManagement/assignments/add-assignment"; // HTML page to add a new assignment
    }

    @PostMapping("/{divisionId}/{semesterId}/save")
    public String saveAssignment(@PathVariable Long divisionId, @PathVariable Long semesterId, @RequestParam(required = false) Integer maxQuestions, @RequestParam String questions, @RequestParam(required = false) boolean randomAssignment, @RequestParam(required = false) MultipartFile file, Model model) {

        if (questions == null || questions.isEmpty()) {
            model.addAttribute("error", "Please provide questions.");
            return "StudentManagement/assignments/add-assignment";
        }
        System.out.println(maxQuestions);
        assignmentService.createAssignment(divisionId, semesterId, maxQuestions, questions, randomAssignment, file);
        return "redirect:/assignments/" + divisionId + "/" + semesterId;
    }

    //    @GetMapping("/assignments/{assignmentId}/assigned-questions")
//    public String getAssignedQuestions(@PathVariable Long assignmentId, Model model) {
    @GetMapping("/{assignmentId}/assigned-questions")
    public String getAssignedQuestions(
            @PathVariable Long assignmentId,
            Model model) {

        // Fetching assigned questions from the service
        List<StudentAssignmentModel> assignedQuestions = assignmentService.getAssignedQuestions(assignmentId);

        // Grouping questions by student ID
        Map<Long, StudentQuestionsDTO> groupedQuestions = new HashMap<>();

        for (StudentAssignmentModel assignment : assignedQuestions) {
            Long studentId = assignment.getStudent().getId();
            String questionText = assignment.getQuestion().getQuestionText();

            // If student is not already in the map, add them
            groupedQuestions.putIfAbsent(studentId, new StudentQuestionsDTO(studentId, assignment.getStudent().getFirstName()));

            // Add the question to the student's list of questions
            groupedQuestions.get(studentId).getQuestions().add(questionText);
        }

        // Convert the map values into a list to pass to the view
        List<StudentQuestionsDTO> studentQuestionsList = new ArrayList<>(groupedQuestions.values());

        // Finding the maximum number of questions assigned to any student
        int maxQuestions = studentQuestionsList.stream()
                .mapToInt(sa -> sa.getQuestions().size())
                .max()
                .orElse(0);

        model.addAttribute("assignedQuestions", studentQuestionsList);
        model.addAttribute("maxQuestions", maxQuestions);

        return "StudentManagement/assignments/assigned-questions";
    }


}