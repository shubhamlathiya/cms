package com.codershubham.cms.cms.repository.ExaminationManagementModules;

import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormStatus;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<ExamModel, Long> {

    List<ExamModel> findByStatus(ExamFormStatus status);

    @Query("SELECT e FROM ExamModel e WHERE e.student = :student")
    List<ExamModel> findByStudent(@Param("student") StudentModel student);

    Optional<ExamModel> findByStudentIdAndExamFormId(Long studentId, Long examFormId);

}
