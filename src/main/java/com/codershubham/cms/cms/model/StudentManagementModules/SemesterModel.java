package com.codershubham.cms.cms.model.StudentManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Semester")
public class SemesterModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // For example, "Fall 2025"

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseModel course;

    // New fields added to match the form data
    @Column(nullable = false)
    private int academicYear; // For example, 2025

    @Column(nullable = false)
    private LocalDate startDate; // Start date for the semester

    @Column(nullable = false)
    private LocalDate endDate; // End date for the semester

    // Constructors, getters, setters

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}