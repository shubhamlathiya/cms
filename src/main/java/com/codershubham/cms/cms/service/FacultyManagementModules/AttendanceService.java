package com.codershubham.cms.cms.service.FacultyManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.DTO.AttendanceRecordDto;
import com.codershubham.cms.cms.model.FacultyManagementModules.AttendanceModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultySubjectAssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.SubjectRepository;
import com.codershubham.cms.cms.repository.FacultyManagementModules.AttendanceRepository;
import com.codershubham.cms.cms.repository.FacultyManagementModules.FacultySubjectAssignmentRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.StudentRepository;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private FacultySubjectAssignmentRepository facultySubjectAssignmentRepository;
    /**
     * Mark attendance for a student with date and time.
     */
    public void markAttendance(Long studentId, Long subjectId, Long facultyId, Long divisionId, int lectureNumber, LocalTime lectureTime, String status, LocalDate date) {
        // Fetch student, subject, faculty, and division from the database
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        SubjectsModel subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectId));

        FacultyModel faculty = facultyService.getFacultyById(facultyId).orElseThrow(() -> new RuntimeException("Faculty not found with ID: " + facultyId));

        DivisionModel division = divisionService.getDivisionById(divisionId);

        // Check if attendance already exists for this student, subject, and lecture
        Optional<AttendanceModel> existingAttendance = attendanceRepository.findByStudentAndSubjectAndLectureNumber(student, subject, lectureNumber);

        AttendanceModel attendance = existingAttendance.orElseGet(() -> {
            AttendanceModel newAttendance = new AttendanceModel();
            newAttendance.setStudent(student);
            newAttendance.setSubject(subject);
            newAttendance.setLectureNumber(lectureNumber);
            newAttendance.setFaculty(faculty);
            newAttendance.setDivision(division);
            return newAttendance;
        });

        // Update values
        attendance.setAttendanceTime(lectureTime);
        attendance.setAttendanceDate(date);
        attendance.setStatus(status);

        // Save attendance record
        attendanceRepository.save(attendance);
    }

    public boolean isAttendanceTaken(Long assignmentId, int lectureNumber) {
        // Fetch assignment details
        Optional<FacultySubjectAssignmentModel> assignmentOpt = facultySubjectAssignmentRepository.findById(assignmentId);
        if (assignmentOpt.isEmpty()) {
            return false; // No assignment found
        }

        FacultySubjectAssignmentModel assignment = assignmentOpt.get();
        Long divisionId = assignment.getDivision().getId();
        Long facultyId = assignment.getFaculty().getFacultyId();
        Long subjectId = assignment.getSubject().getSubjectid();

        // Check if attendance exists
        return attendanceRepository.existsByDivisionIdAndFacultyIdAndSubjectIdAndLectureNumber(
                divisionId, facultyId, subjectId, lectureNumber);
    }

    public List<AttendanceModel> getAttendanceDetails(Long assignmentId, int lectureNumber) {
        // Fetch assignment details
        Optional<FacultySubjectAssignmentModel> assignmentOpt = facultySubjectAssignmentRepository.findById(assignmentId);
        if (assignmentOpt.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if no assignment is found
        }

        FacultySubjectAssignmentModel assignment = assignmentOpt.get();
        Long divisionId = assignment.getDivision().getId();
        Long facultyId = assignment.getFaculty().getFacultyId();
        Long subjectId = assignment.getSubject().getSubjectid();

        // Fetch attendance records
        return attendanceRepository.findByDivisionIdAndFacultyIdAndSubjectIdAndLectureNumber(
                divisionId, facultyId, subjectId, lectureNumber);
    }
    public int countPresentLectures(Long studentId, Long subjectId) {
        return attendanceRepository.countByStudentIdAndSubjectIdAndStatus(studentId, subjectId);
    }

    public int getTotalLecturesBySubjectAndDivision(Long divisionId, Long subjectId) {
        return attendanceRepository.countTotalLecturesBySubjectAndDivision(divisionId, subjectId);
    }

    public List<AttendanceRecordDto> getAttendanceByStudentAndSubject(Long studentId, Long divisionId, Long subjectId) {
        List<AttendanceModel> attendanceModels = attendanceRepository.findByStudentIdAndDivisionIdAndSubjectId(studentId, divisionId, subjectId);

        // Convert AttendanceModel to AttendanceRecordDto
        return attendanceModels.stream()
                .map(attendance -> new AttendanceRecordDto(
                        attendance.getStudent().getId(),
                        attendance.getDivision().getId(),
                        attendance.getSubject().getSubjectid(),
                        attendance.getLectureNumber(),
                        attendance.getAttendanceDate().toString(),  // Formatting Date
                        attendance.getAttendanceTime().toString(),  // Formatting Time
                        attendance.getStatus()))
                .collect(Collectors.toList());
    }

}
