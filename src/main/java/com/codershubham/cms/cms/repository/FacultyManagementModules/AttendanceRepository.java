package com.codershubham.cms.cms.repository.FacultyManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.AttendanceModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceModel, Long> {

    List<AttendanceModel> findByFaculty(FacultyModel faculty);

    Optional<AttendanceModel> findByStudentAndSubjectAndLectureNumber(StudentModel student, SubjectsModel subject, int lectureNumber);

    @Query("SELECT a FROM AttendanceModel a WHERE a.subject.subjectid = :subjectId")
    List<AttendanceModel> findBySubjectId(@Param("subjectId") Long subjectId);


    @Query("SELECT COUNT(a) FROM AttendanceModel a WHERE a.subject.subjectid = :subjectId AND a.lectureNumber = :lectureNumber AND a.division.id = :divisionId")
    int countBySubjectIdAndLectureNumberAndDivisionId(@Param("subjectId") Long subjectId, @Param("lectureNumber") int lectureNumber, @Param("divisionId") Long divisionId);


    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " + "FROM AttendanceModel a " + "WHERE a.division.id = :divisionId " + "AND a.faculty.facultyId = :facultyId " + "AND a.subject.subjectid = :subjectId " + "AND a.lectureNumber = :lectureNumber")
    boolean existsByDivisionIdAndFacultyIdAndSubjectIdAndLectureNumber(@Param("divisionId") Long divisionId, @Param("facultyId") Long facultyId, @Param("subjectId") Long subjectId, @Param("lectureNumber") int lectureNumber);


    @Query("SELECT a FROM AttendanceModel a " + "WHERE a.division.id = :divisionId " + "AND a.faculty.facultyId = :facultyId " + "AND a.subject.subjectid = :subjectId " + "AND a.lectureNumber = :lectureNumber")
    List<AttendanceModel> findByDivisionIdAndFacultyIdAndSubjectIdAndLectureNumber(@Param("divisionId") Long divisionId, @Param("facultyId") Long facultyId, @Param("subjectId") Long subjectId, @Param("lectureNumber") int lectureNumber);


    @Query("SELECT a FROM AttendanceModel a WHERE a.lectureNumber = :lectureNumber " + "AND a.student.id = :studentId " + "AND a.faculty.facultyId = :facultyId " + "AND a.division.id = :divisionId " + "AND a.subject.subjectid = :subjectId")
    Optional<AttendanceModel> findByLectureNumberAndStudentIdAndFacultyIdAndDivisionIdAndSubjectId(@Param("lectureNumber") Integer lectureNumber, @Param("studentId") Long studentId, @Param("facultyId") Long facultyId, @Param("divisionId") Long divisionId, @Param("subjectId") Long subjectId);
}
