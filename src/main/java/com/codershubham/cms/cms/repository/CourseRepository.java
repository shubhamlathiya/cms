package com.codershubham.cms.cms.repository;

import com.codershubham.cms.cms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDepartmentId(Long departmentId);

}

