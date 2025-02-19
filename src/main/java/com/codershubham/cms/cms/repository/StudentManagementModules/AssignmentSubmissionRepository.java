package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.AssignmentSubmissionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmissionModel, Long> {

    // Find submission by student and assignment ID
    Optional<AssignmentSubmissionModel> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

    // Find all submissions for a particular assignment
    List<AssignmentSubmissionModel> findByAssignmentId(Long assignmentId);

    // Find all submissions for a particular student
    List<AssignmentSubmissionModel> findByStudentId(Long studentId);

    @Query("SELECT a FROM AssignmentSubmissionModel a WHERE a.assignment.id = :assignmentId AND a.student.id = :studentId")
    AssignmentSubmissionModel findByAssignmentIdAndStudentId(@Param("assignmentId") Long assignmentId, @Param("studentId") Long studentId);

}
