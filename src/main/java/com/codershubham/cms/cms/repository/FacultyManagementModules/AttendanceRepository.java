package com.codershubham.cms.cms.repository.FacultyManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.DTO.AttendanceRecordDto;
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

    Optional<AttendanceModel> findByStudentAndSubjectAndLectureNumber(StudentModel student, SubjectsModel subject, int lectureNumber);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " + "FROM AttendanceModel a " + "WHERE a.division.id = :divisionId " + "AND a.faculty.facultyId = :facultyId " + "AND a.subject.subjectid = :subjectId " + "AND a.lectureNumber = :lectureNumber")
    boolean existsByDivisionIdAndFacultyIdAndSubjectIdAndLectureNumber(@Param("divisionId") Long divisionId, @Param("facultyId") Long facultyId, @Param("subjectId") Long subjectId, @Param("lectureNumber") int lectureNumber);


    @Query("SELECT a FROM AttendanceModel a " + "WHERE a.division.id = :divisionId " + "AND a.faculty.facultyId = :facultyId " + "AND a.subject.subjectid = :subjectId " + "AND a.lectureNumber = :lectureNumber")
    List<AttendanceModel> findByDivisionIdAndFacultyIdAndSubjectIdAndLectureNumber(@Param("divisionId") Long divisionId, @Param("facultyId") Long facultyId, @Param("subjectId") Long subjectId, @Param("lectureNumber") int lectureNumber);


    @Query("SELECT a FROM AttendanceModel a WHERE a.student.id = :studentId AND a.subject.subjectid = :subjectId")
    List<AttendanceModel> findByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    @Query("SELECT COUNT(a) FROM AttendanceModel a WHERE a.student.id = :studentId AND a.subject.subjectid = :subjectId AND a.status = 'Present'")
    int countByStudentIdAndSubjectIdAndStatus(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);

    @Query("SELECT COUNT(DISTINCT a.lectureNumber) FROM AttendanceModel a WHERE a.division.id = :divisionId AND a.subject.subjectid = :subjectId")
    int countTotalLecturesBySubjectAndDivision(@Param("divisionId") Long divisionId, @Param("subjectId") Long subjectId);

    @Query("SELECT a FROM AttendanceModel a " +
            "WHERE a.student.id = :studentId AND a.division.id = :divisionId AND a.subject.subjectid = :subjectId")
    List<AttendanceModel> findByStudentIdAndDivisionIdAndSubjectId(@Param("studentId") Long studentId,
                                                                   @Param("divisionId") Long divisionId,
                                                                   @Param("subjectId") Long subjectId);


}
