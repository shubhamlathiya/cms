package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentModel, Long> {
    // Add custom query methods if necessary
    boolean existsByEmail(String email);

    //    List<StudentModel> findByCourseId(Long courseId);
    Optional<StudentModel> findByUserId(Long userId);

//    List<StudentModel> findByCourse(CourseModel course);

//    //     Fetch students who are NOT assigned to any semester or division
//    @Query("SELECT s FROM StudentModel s WHERE s.id NOT IN (SELECT se.student.id FROM StudentEnrollmentModel se)")
//    List<StudentModel> findUnassignedStudents();

//    List<StudentModel> findByDivisionId(Long divisionId);

    @Query("SELECT s FROM StudentModel s WHERE s.course.courseID = " +
            "(SELECT sem.course.courseID FROM SemesterModel sem WHERE sem.id = :semesterId) " +
            "AND s.id NOT IN (SELECT e.student.id FROM StudentEnrollmentModel e WHERE e.semester.id = :semesterId)")
    List<StudentModel> findUnassignedStudentsBySemester(@Param("semesterId") Long semesterId);
}
