package com.codershubham.cms.cms.model.DTO;


import java.util.List;
import java.util.Date;

public class SubjectEnrollmentRequestDto {
    private List<Long> studentIds;
    private List<Long> subjectIds;
    private Long courseId;
    private Long semesterId;  // ✅ Should be Long, NOT int

    private Date enrollmentDate;

    // Getters and Setters
    public List<Long> getStudentIds() { return studentIds; }
    public void setStudentIds(List<Long> studentIds) { this.studentIds = studentIds; }

    public List<Long> getSubjectIds() { return subjectIds; }
    public void setSubjectIds(List<Long> subjectIds) { this.subjectIds = subjectIds; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Date getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Date enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}
