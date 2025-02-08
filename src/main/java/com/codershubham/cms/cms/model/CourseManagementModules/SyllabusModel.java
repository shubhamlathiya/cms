package com.codershubham.cms.cms.model.CourseManagementModules;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Syllabus")
public class SyllabusModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long syllabusid;

    @Column(nullable = false)
    private int unitNo;

    @Column(nullable = false)
    private String unitName;

    @ElementCollection
    private List<String> topics; // Store topics as a list (JSON)

    @Column(nullable = true)
    private String resources;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectsModel subject;

    public Long getSyllabusid() {
        return syllabusid;
    }

    public void setSyllabusid(Long syllabusid) {
        this.syllabusid = syllabusid;
    }

    public int getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(int unitNo) {
        this.unitNo = unitNo;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public SubjectsModel getSubject() {
        return subject;
    }

    public void setSubject(SubjectsModel subject) {
        this.subject = subject;
    }
}
