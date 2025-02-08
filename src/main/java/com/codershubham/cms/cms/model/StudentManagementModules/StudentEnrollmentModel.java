package com.codershubham.cms.cms.model.StudentManagementModules;

import jakarta.persistence.*;

@Entity
@Table(name = "StudentEnrollments")
public class StudentEnrollmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id") // Foreign key to Student
    private StudentModel student;

    @ManyToOne
    @JoinColumn(name = "semester_id")  // Foreign key to Semester
    private SemesterModel semester;

    @ManyToOne
    @JoinColumn(name = "division_id")  // Foreign key to Division
    private DivisionModel division;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }

    public SemesterModel getSemester() {
        return semester;
    }

    public void setSemester(SemesterModel semester) {
        this.semester = semester;
    }

    public DivisionModel getDivision() {
        return division;
    }

    public void setDivision(DivisionModel division) {
        this.division = division;
    }
}
