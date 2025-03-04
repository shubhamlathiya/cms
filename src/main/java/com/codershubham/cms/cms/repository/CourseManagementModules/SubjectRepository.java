package com.codershubham.cms.cms.repository.CourseManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<SubjectsModel, Long> {
    List<SubjectsModel> findByCourseModel(CourseModel courseModel);

//    List<SubjectsModel> findAllById(List<Long> ids);
//    @Query("SELECT s FROM SubjectsModel s WHERE s.semester.id = :semesterId AND s.faculty IS NOT NULL")
//    List<SubjectsModel> findSubjectsAssignedToFaculty(@Param("semesterId") Long semesterId);
//
//    @Query("SELECT s FROM SubjectsModel s WHERE s.semester.id = :semesterId AND s.faculty IS NULL")
//    List<SubjectsModel> findSubjectsNotAssignedToFaculty(@Param("semesterId") Long semesterId);
}
