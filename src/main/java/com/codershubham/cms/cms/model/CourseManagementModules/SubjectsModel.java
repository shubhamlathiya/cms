package com.codershubham.cms.cms.model.CourseManagementModules;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Subjects")
public class SubjectsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjectid")  // Ensure this matches the column name in the DB
    private Long subjectid;

    private String subjectCode;
    private String subjectName;
    private int credits;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "courseID")
    private CourseModel courseModel;

    public Long getSubjectid() {
        return subjectid;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public void setSubjectid(Long subjectid) {
        this.subjectid = subjectid;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CourseModel getCourse() {
        return courseModel;
    }

    public void setCourse(CourseModel courseModel) {
        this.courseModel = courseModel;
    }
}