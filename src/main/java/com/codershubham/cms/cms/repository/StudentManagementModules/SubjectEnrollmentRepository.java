package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SubjectEnrollmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectEnrollmentRepository extends JpaRepository<SubjectEnrollmentModel, Long> {

    List<SubjectEnrollmentModel> findByStudentIdAndSemesterId(Long studentId, Long semesterId);
}

