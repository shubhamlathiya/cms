package com.codershubham.cms.cms.repository.CourseManagementModules;


import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentModel, Long> {
    Optional<DepartmentModel> findById(Long id);
}