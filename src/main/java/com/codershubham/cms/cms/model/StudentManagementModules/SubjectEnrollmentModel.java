package com.codershubham.cms.cms.model.StudentManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import jakarta.persistence.*;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student_subjects")
public class SubjectEnrollmentModel {

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
    @JoinColumn(name = "course_id", nullable = false)
    private CourseModel course;

    @Column(name = "semester", nullable = false)
    private int semester;

    @Column(name = "enrollment_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enrollmentDate;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentModel getStudent() { return student; }
    public void setStudent(StudentModel student) { this.student = student; }

    public SubjectsModel getSubject() { return subject; }
    public void setSubject(SubjectsModel subject) { this.subject = subject; }

    public CourseModel getCourse() { return course; }
    public void setCourse(CourseModel course) { this.course = course; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public Date getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Date enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}
