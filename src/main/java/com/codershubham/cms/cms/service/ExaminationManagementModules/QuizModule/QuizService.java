package com.codershubham.cms.cms.service.ExaminationManagementModules.QuizModule;


import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizAttemptModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule.QuizAttemptRepository;
import com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    public QuizModel getQuizById(Long quizId) {
        return quizRepository.findById(quizId).orElse(null);
    }

    public boolean hasStudentAttemptedQuiz(StudentModel student, QuizModel quiz) {
        return quizAttemptRepository.existsByStudentAndQuiz(student, quiz);
    }

    public List<QuizModel> getQuizzesForSubjectAndDivision(Long subjectId, Long divisionId, Long semesterId) {
        return quizRepository.findBySubjectIdAndDivisionIdAndSemesterId(subjectId, divisionId, semesterId);
    }

    public List<QuizModel> getAssignmentsByDivisionSemesterAndSubject(Long divisionId, Long semesterId, Long subjectId) {
        return quizRepository.findByDivisionIdAndSemesterIdAndSubjectId(divisionId, semesterId, subjectId);
    }

    // Create a new quiz
    public QuizModel createQuiz(String title, LocalDate quizDate, LocalTime startTime, LocalTime endTime, Integer totalMarks, Long facultyId, Long subjectId, Long semesterId, Long divisionId) {
        QuizModel quiz = new QuizModel();
        quiz.setTitle(title);
        quiz.setQuizDate(quizDate);
        quiz.setStartTime(startTime);
        quiz.setEndTime(endTime);
        quiz.setTotalMarks(totalMarks);
        quiz.setFacultyId(facultyId);
        quiz.setSubjectId(subjectId);
        quiz.setDivisionId(divisionId);
        quiz.setSemesterId(semesterId);
        return quizRepository.save(quiz);
    }

    // Fetch quizzes by faculty ID
    public List<QuizModel> getQuizzesByFacultyId(Long facultyId) {
        return quizRepository.findByFacultyId(facultyId);
    }

    public boolean hasAssignmentsForSubjectAndDivision(Long subjectId, Long divisionId) {
        return quizRepository.quizExistsBySubjectIdAndDivisionId(subjectId, divisionId);
    }

//    public List<QuizAttemptModel> getQuizAttemptsForQuiz(Long quizId, List<Long> studentIds) {
//        if (studentIds == null || studentIds.isEmpty()) {
//            return quizRepository.findById(quizId).orElse(null);
//        } else {
//            return quizAttemptRepository.findByQuizIdWithDetails(quizId, studentIds);
//        }
//    }

}