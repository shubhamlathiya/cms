package com.codershubham.cms.cms.model.FacultyManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
public class AttendanceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentModel student;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectsModel subject;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyModel faculty;

    @ManyToOne
    @JoinColumn(name = "division_id", nullable = false)
    private DivisionModel division;

    private int lectureNumber;
    private LocalDate attendanceDate;
    private LocalTime attendanceTime;
    private String status; // Present or Absent

    // Constructors
    public AttendanceModel() {}

    public AttendanceModel(StudentModel student, SubjectsModel subject, FacultyModel faculty, DivisionModel division,
                           int lectureNumber, String status) {
        this.student = student;
        this.subject = subject;
        this.faculty = faculty;
        this.division = division;
        this.lectureNumber = lectureNumber;
        this.attendanceDate = LocalDate.now();
        this.attendanceTime = LocalTime.now();
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentModel getStudent() { return student; }
    public void setStudent(StudentModel student) { this.student = student; }

    public SubjectsModel getSubject() { return subject; }
    public void setSubject(SubjectsModel subject) { this.subject = subject; }

    public FacultyModel getFaculty() { return faculty; }
    public void setFaculty(FacultyModel faculty) { this.faculty = faculty; }

    public DivisionModel getDivision() { return division; }
    public void setDivision(DivisionModel division) { this.division = division; }

    public int getLectureNumber() { return lectureNumber; }
    public void setLectureNumber(int lectureNumber) { this.lectureNumber = lectureNumber; }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

    public LocalTime getAttendanceTime() { return attendanceTime; }
    public void setAttendanceTime(LocalTime attendanceTime) { this.attendanceTime = attendanceTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
