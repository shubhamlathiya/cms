package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.StudentAssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentEnrollmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Collectors;

public interface StudentAssignmentRepository extends JpaRepository<StudentAssignmentModel, Long> {

    @Query("SELECT sa FROM StudentAssignmentModel sa " +
            "WHERE sa.assignment.id = :assignmentId")
    List<StudentAssignmentModel> findByAssignmentId(@Param("assignmentId") Long assignmentId);

    @Query("SELECT sa FROM StudentAssignmentModel sa " +
            "WHERE sa.student.id IN :studentIds")
    List<StudentAssignmentModel> findByStudents(@Param("studentIds") List<Long> studentIds);


    @Query("SELECT sa FROM StudentAssignmentModel sa WHERE sa.student.id = :studentId AND sa.assignment.subjectId = :subjectId")
    List<StudentAssignmentModel> findByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);
}
