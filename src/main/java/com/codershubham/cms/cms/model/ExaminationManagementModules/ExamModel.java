package com.codershubham.cms.cms.model.ExaminationManagementModules;


import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "exams")
public class ExamModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_form_id", nullable = false)
    private ExamFormModel examForm;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentModel student;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private FacultyModel approvedByFaculty; // Faculty who approved the exam form

    @Column(name = "fee_amount", nullable = false)
    private Double feeAmount;

    @Column(name = "late_fee", nullable = true)
    private Double lateFee;

    @Column(name = "super_late_fee", nullable = true)
    private Double superLateFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ExamFormStatus status = ExamFormStatus.PENDING_APPROVAL;

    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @Column(name = "payment_status", nullable = false)
    private Boolean paymentStatus = false;

    @ManyToMany
    @JoinTable(
            name = "exam_form_subjects",
            joinColumns = @JoinColumn(name = "exam_form_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<SubjectsModel> subjects; // List of subjects for this exam form

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExamFormModel getExamForm() {
        return examForm;
    }

    public void setExamForm(ExamFormModel examForm) {
        this.examForm = examForm;
    }

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }

    public FacultyModel getApprovedByFaculty() {
        return approvedByFaculty;
    }

    public void setApprovedByFaculty(FacultyModel approvedByFaculty) {
        this.approvedByFaculty = approvedByFaculty;
    }

    public Double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Double getLateFee() {
        return lateFee;
    }

    public void setLateFee(Double lateFee) {
        this.lateFee = lateFee;
    }

    public Double getSuperLateFee() {
        return superLateFee;
    }

    public void setSuperLateFee(Double superLateFee) {
        this.superLateFee = superLateFee;
    }

    public ExamFormStatus getStatus() {
        return status;
    }

    public void setStatus(ExamFormStatus status) {
        this.status = status;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<SubjectsModel> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsModel> subjects) {
        this.subjects = subjects;
    }
}

