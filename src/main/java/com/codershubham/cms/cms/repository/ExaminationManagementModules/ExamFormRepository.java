package com.codershubham.cms.cms.repository.ExaminationManagementModules;


import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormStatus;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExamFormRepository extends JpaRepository<ExamFormModel, Long> {

    @Query("SELECT ef FROM ExamFormModel ef WHERE ef.course.courseID = :courseId AND ef.semester.id = :semesterId")
    ExamFormModel findByCourseIdAndSemesterId(@Param("courseId") Long courseId, @Param("semesterId") Long semesterId);

}