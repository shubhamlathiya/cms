package com.codershubham.cms.cms.repository.ExaminationManagementModules;


import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExamRepository extends JpaRepository<ExamFormModel, Long> {

    @Query("SELECT ef FROM ExamFormModel ef WHERE ef.course.courseID = :courseId AND ef.semester.id = :semesterId")
    ExamFormModel findByCourseIdAndSemesterId(@Param("courseId") Long courseId, @Param("semesterId") Long semesterId);

}