package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.FacultyManagementModules.AttendanceModel;
import com.codershubham.cms.cms.model.StudentManagementModules.AssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.QuestionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentAssignmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<AssignmentModel, Long> {

    List<AssignmentModel> findByDivisionIdAndSemesterId(Long divisionId, Long semesterId);
}
