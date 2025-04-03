package com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule;

import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizAttemptModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizAttemptRepository extends JpaRepository<QuizAttemptModel, Long> {
    boolean existsByStudentAndQuiz(StudentModel student, QuizModel quiz);

    Optional<QuizAttemptModel> findByStudentIdAndQuizId(Long studentId, Long quizId);

    // If you need to fetch attempts with student and answer data
//    @Query("SELECT qa FROM QuizAttemptModel qa " +
//            "LEFT JOIN FETCH qa.student " +
//            "LEFT JOIN FETCH qa.answers " +
//            "WHERE qa.quiz.id = :quizId")
//    List<QuizAttemptModel> findByQuizIdWithDetails(@Param("quizId") Long quizId);
}
