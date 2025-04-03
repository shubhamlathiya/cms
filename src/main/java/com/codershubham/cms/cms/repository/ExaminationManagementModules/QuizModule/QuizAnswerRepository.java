package com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule;

import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizAnswerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswerModel, Long> {
}
