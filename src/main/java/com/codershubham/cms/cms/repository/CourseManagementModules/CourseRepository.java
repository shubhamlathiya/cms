package com.codershubham.cms.cms.repository.CourseManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseModel, Long> {
    List<CourseModel> findByDepartmentId(Long departmentId);

}

