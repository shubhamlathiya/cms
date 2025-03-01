package com.codershubham.cms.cms.model.ExaminationManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "exam_forms")
public class ExamFormModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_form_name", nullable = false)
    private String examFormName;

    @Enumerated(EnumType.STRING)
    @Column(name = "form_type", nullable = false)
    private FormType formType;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseModel course;

    @Column(name = "year", nullable = false)
    private String year;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date_with_late_fee", nullable = false)
    private Date endDateWithLateFee;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date_with_super_late_fee", nullable = false)
    private Date endDateWithSuperLateFee;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentModel department;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private SemesterModel semester;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamFormName() {
        return examFormName;
    }

    public void setExamFormName(String examFormName) {
        this.examFormName = examFormName;
    }

    public FormType getFormType() {
        return formType;
    }

    public void setFormType(FormType formType) {
        this.formType = formType;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDateWithLateFee() {
        return endDateWithLateFee;
    }

    public void setEndDateWithLateFee(Date endDateWithLateFee) {
        this.endDateWithLateFee = endDateWithLateFee;
    }

    public Date getEndDateWithSuperLateFee() {
        return endDateWithSuperLateFee;
    }

    public void setEndDateWithSuperLateFee(Date endDateWithSuperLateFee) {
        this.endDateWithSuperLateFee = endDateWithSuperLateFee;
    }

    public DepartmentModel getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentModel department) {
        this.department = department;
    }

    public SemesterModel getSemester() {
        return semester;
    }

    public void setSemester(SemesterModel semester) {
        this.semester = semester;
    }

    public enum FormType {
        REGULAR,
        BACKLOG,
        REVALUATION
    }
}
