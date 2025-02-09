package com.codershubham.cms.cms.repository.CourseManagementModules;


import com.codershubham.cms.cms.model.CourseManagementModules.SyllabusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SyllabusRepository extends JpaRepository<SyllabusModel, Long> {
    List<SyllabusModel> findBySubjectSubjectid(Long subjectId);
}
