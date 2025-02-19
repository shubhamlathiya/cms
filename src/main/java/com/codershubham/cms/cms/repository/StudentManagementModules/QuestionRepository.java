package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.QuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionModel, Long> {

    List<QuestionModel> findByAssignmentId(Long assignmentId);
}
