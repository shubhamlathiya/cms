package com.codershubham.cms.cms.model.StudentManagementModules;

import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import jakarta.persistence.*;

@Entity
@Table(name = "Division")
public class DivisionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // For example: "Group A", "Group B"

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterModel semester; // Division belongs to a specific semester

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private FacultyModel faculty;
    // Constructors, getters, setters


    public FacultyModel getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyModel faculty) {
        this.faculty = faculty;
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

    public SemesterModel getSemester() {
        return semester;
    }

    public void setSemester(SemesterModel semester) {
        this.semester = semester;
    }
}
