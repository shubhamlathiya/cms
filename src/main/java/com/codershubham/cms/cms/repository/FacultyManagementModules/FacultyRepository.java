package com.codershubham.cms.cms.repository.FacultyManagementModules;

import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<FacultyModel, Long> {
    boolean existsByEmail(String email);

    Optional<FacultyModel> findByUserId(Long userId);
    List<FacultyModel> findByDepartmentId(Long departmentId);

//    Optional<Faculty> findByUsername(String username);
}
