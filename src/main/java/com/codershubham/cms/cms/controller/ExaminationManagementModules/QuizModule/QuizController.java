package com.codershubham.cms.cms.controller.ExaminationManagementModules.QuizModule;

import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizAttemptModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizQuestionModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.service.ExaminationManagementModules.QuizModule.QuizQuestionService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.QuizModule.QuizService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizQuestionService questionService;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private HttpSession session;

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/{divisionId}/{semesterId}/{subjectId}")
    public String getAssignments(@PathVariable Long divisionId, @PathVariable Long semesterId, @PathVariable Long subjectId, Model model) {
        List<QuizModel> quizs = quizService.getAssignmentsByDivisionSemesterAndSubject(divisionId, semesterId, subjectId);
        model.addAttribute("quizs", quizs);
        model.addAttribute("divisionId", divisionId);
        model.addAttribute("semesterId", semesterId);
        model.addAttribute("subjectId", subjectId);

        Long userId = (Long) session.getAttribute("userId");

        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        model.addAttribute("faculty", faculty);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        return "FacultyManagement/QuizModule/quizzes";
    }

    // Show questions for a quiz
    @GetMapping("/questions")
    public String showQuestions(@RequestParam Long quizId, Model model) {
        List<QuizQuestionModel> questions = questionService.getQuestionsByQuizId(quizId);
        model.addAttribute("questions", questions);
        model.addAttribute("quizId", quizId);

        Long userId = (Long) session.getAttribute("userId");

        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        model.addAttribute("faculty", faculty);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        
        return "FacultyManagement/QuizModule/questions";
    }

    @PostMapping("/questions/upload")
    public String uploadExcel(@RequestParam("excelFile") MultipartFile file,@RequestParam Long quizId) {
        if (!file.isEmpty()) {
            questionService.uploadQuestionsFromExcel(file ,quizId);
        }
        return "redirect:/quizzes/questions?quizId=" + quizId; // Redirect to the list of questions
    }
    // Create a new quiz
    @PostMapping("/add")
    public String createQuiz(@RequestParam String title, @RequestParam LocalDate quizDate, @RequestParam LocalTime startTime, @RequestParam LocalTime endTime, @RequestParam Integer totalMarks, @RequestParam Long facultyId, @RequestParam Long subjectId, @RequestParam Long semesterId, @RequestParam Long divisionId) {
        quizService.createQuiz(title, quizDate, startTime, endTime, totalMarks, facultyId, subjectId, semesterId, divisionId);
        return "redirect:/faculty/subjects/" + facultyId;
    }

    // Add a question to a quiz
    @PostMapping("/questions/add")
    public String addQuestion(@RequestParam Long quizId, @RequestParam String questionText, @RequestParam String option1, @RequestParam String option2, @RequestParam String option3, @RequestParam String option4, @RequestParam Integer correctOption) {
        questionService.addQuestion(quizId, questionText, option1, option2, option3, option4, correctOption);
        return "redirect:/quizzes/questions?quizId=" + quizId;
    }

    // Edit a quiz question
    @PostMapping("/questions/edit")
    public String editQuestion(@RequestParam Long questionId, @RequestParam String questionText,
                               @RequestParam String option1, @RequestParam String option2,
                               @RequestParam String option3, @RequestParam String option4,
                               @RequestParam Integer correctOption, @RequestParam Long quizId) {
        questionService.updateQuestion(questionId, questionText, option1, option2, option3, option4, correctOption);
        return "redirect:/quizzes/questions?quizId=" + quizId;
    }

    // Delete a quiz question
    @PostMapping("/questions/delete")
    public String deleteQuestion(@RequestParam Long questionId, @RequestParam Long quizId) {
        questionService.deleteQuestion(questionId);
        return "redirect:/quizzes/questions?quizId=" + quizId;
    }

    // Add this method to your QuizController class
//    @GetMapping("/student-scores")
//    public String showStudentScores(
//            @RequestParam Long quizId,
//            @RequestParam(required = false) List<Long> studentIds,
//            Model model) {
//
//        // Get all quiz attempts for the quiz (filtered by studentIds if provided)
//        List<QuizAttemptModel> quizAttempts = quizService.getQuizAttemptsForQuiz(quizId, studentIds);
//
//        // Get quiz details
//        QuizModel quiz = quizService.getQuizById(quizId);
//
//        model.addAttribute("quizAttempts", quizAttempts);
//        model.addAttribute("quiz", quiz);
//
//        // Add user role and faculty info
//        Long userId = (Long) session.getAttribute("userId");
//        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
//        model.addAttribute("faculty", faculty);
//
//        String userRole = userRoleUtil.getUserRole(session);
//        model.addAttribute("userRole", userRole);
//
//        return "FacultyManagement/QuizModule/studentScores";
//    }

//    @GetMapping("/attempt-details")
//    public String showAttemptDetails(@RequestParam Long id, Model model) {
//        QuizAttemptModel attempt = quizService.getAttemptByIdWithAnswers(id);
//        model.addAttribute("attempt", attempt);
//
//        // Add user role and faculty info
//        Long userId = (Long) session.getAttribute("userId");
//        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
//        model.addAttribute("faculty", faculty);
//
//        String userRole = userRoleUtil.getUserRole(session);
//        model.addAttribute("userRole", userRole);
//
//        return "FacultyManagement/QuizModule/attemptDetails";
//    }
}