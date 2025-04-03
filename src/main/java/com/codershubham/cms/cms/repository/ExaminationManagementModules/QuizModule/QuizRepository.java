package com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule;


import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizModel;
import com.codershubham.cms.cms.model.StudentManagementModules.AssignmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<QuizModel, Long> {

    List<QuizModel> findByFacultyId(Long facultyId);

    List<QuizModel> findByDivisionIdAndSemesterIdAndSubjectId(Long divisionId, Long semesterId, Long subjectId);

    List<QuizModel> findBySubjectIdAndDivisionIdAndSemesterId(Long subjectId, Long divisionId, Long semesterId);

    @Query("SELECT COUNT(a) > 0 FROM QuizModel a WHERE a.subjectId = :subjectId AND a.divisionId = :divisionId")
    boolean quizExistsBySubjectIdAndDivisionId(@Param("subjectId") Long subjectId, @Param("divisionId") Long divisionId);

//    @Query("SELECT s.studentId, s.studentName, qs.score " +
//            "FROM StudentModel s JOIN  qs ON s.studentId = qs.student.studentId " +
//            "WHERE qs.quiz.quizId = :quizId")
//    List<Object[]> findStudentScoresByQuizId(@Param("quizId") Long quizId);
//
//    @Query("SELECT s.studentId, s.studentName, qs.score " +
//            "FROM StudentModel s JOIN QuizSubmissionModel qs ON s.studentId = qs.student.studentId " +
//            "WHERE qs.quiz.quizId = :quizId AND s.studentId IN :studentIds")
//    List<Object[]> findStudentScoresByQuizIdAndStudentIds(
//            @Param("quizId") Long quizId,
//            @Param("studentIds") List<Long> studentIds);
}