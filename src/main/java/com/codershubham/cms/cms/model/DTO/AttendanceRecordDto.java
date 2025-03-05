package com.codershubham.cms.cms.model.DTO;


import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceRecordDto {

    private Long studentId;
    private Long divisionId;
    private Long subjectId;
    private int lectureNumber;
    private String attendanceDate;
    private String attendanceTime;
    private String status;

    // Constructor
    public AttendanceRecordDto(Long studentId, Long divisionId, Long subjectId, int lectureNumber,
                               String attendanceDate, String attendanceTime, String status) {
        this.studentId = studentId;
        this.divisionId = divisionId;
        this.subjectId = subjectId;
        this.lectureNumber = lectureNumber;
        this.attendanceDate = attendanceDate;
        this.attendanceTime = attendanceTime;
        this.status = status;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Long divisionId) {
        this.divisionId = divisionId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public int getLectureNumber() {
        return lectureNumber;
    }

    public void setLectureNumber(int lectureNumber) {
        this.lectureNumber = lectureNumber;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
