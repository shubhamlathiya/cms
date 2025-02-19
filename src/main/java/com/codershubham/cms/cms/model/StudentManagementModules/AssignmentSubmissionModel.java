package com.codershubham.cms.cms.model.StudentManagementModules;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AssignmentSubmission")
public class AssignmentSubmissionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private AssignmentModel assignment;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private StudentModel student;

    private String submissionFilePath; // Submission file
    private int obtainedMarks; // Marks obtained by student
    private String remarks; // Remarks provided by the teacher

    private LocalDateTime submittedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getSubmissionFilePath() {
        return submissionFilePath;
    }

    public void setSubmissionFilePath(String submissionFilePath) {
        this.submissionFilePath = submissionFilePath;
    }

    public int getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(int obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public AssignmentModel getAssignment() {
        return assignment;
    }

    public void setAssignment(AssignmentModel assignment) {
        this.assignment = assignment;
    }

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }
}
