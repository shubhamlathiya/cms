package com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule;

import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizQuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestionModel, Long> {
    List<QuizQuestionModel> findByQuizId(Long quizId);
}