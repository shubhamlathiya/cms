package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentEnrollmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollmentModel, Long> {

    // Get students by semester
    List<StudentEnrollmentModel> findBySemester(SemesterModel semester);

    // Get students by division
    List<StudentEnrollmentModel> findByDivision(DivisionModel division);

    // Get students by semester and division
    List<StudentEnrollmentModel> findBySemesterAndDivision(SemesterModel semester, DivisionModel division);
    List<StudentEnrollmentModel> findByDivisionId(Long divisionId);


    @Query("SELECT se FROM StudentEnrollmentModel se " +
            "WHERE se.division.id = :divisionId AND se.semester.id = :semesterId")
    List<StudentEnrollmentModel> findByDivisionIdAndSemesterId(
            @Param("divisionId") Long divisionId,
            @Param("semesterId") Long semesterId);
}
