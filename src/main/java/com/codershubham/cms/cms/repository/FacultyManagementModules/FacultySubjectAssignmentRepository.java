package com.codershubham.cms.cms.repository.FacultyManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultySubjectAssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultySubjectAssignmentRepository extends JpaRepository<FacultySubjectAssignmentModel, Long> {

    boolean existsBySubjectAndDivision(SubjectsModel subject, DivisionModel division);


}
