package com.codershubham.cms.cms.repository.CourseManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<SubjectsModel, Long> {
//    Optional<Subjects> findByName(String name);
}
