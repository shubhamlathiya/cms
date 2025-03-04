package com.codershubham.cms.cms.model.ExaminationManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import jakarta.persistence.*;

@Entity
@Table(name = "exam_form_subjects")
public class ExamFormSubjects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_form_detail_id", nullable = false)
    private ExamModel examFormDetails;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectsModel subject;

    // Constructors, Getters, and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExamModel getExamFormDetails() {
        return examFormDetails;
    }

    public void setExamFormDetails(ExamModel examFormDetails) {
        this.examFormDetails = examFormDetails;
    }

    public SubjectsModel getSubject() {
        return subject;
    }

    public void setSubject(SubjectsModel subject) {
        this.subject = subject;
    }
}
