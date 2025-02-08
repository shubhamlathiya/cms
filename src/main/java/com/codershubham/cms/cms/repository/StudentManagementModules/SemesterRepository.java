package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<SemesterModel, Long> {
    // You can add more custom queries here if needed
    List<SemesterModel> findByCourse(CourseModel course);
}
