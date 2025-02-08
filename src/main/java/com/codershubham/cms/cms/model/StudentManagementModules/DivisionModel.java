package com.codershubham.cms.cms.model.StudentManagementModules;

import jakarta.persistence.*;

@Entity
@Table(name = "Division")
public class DivisionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // For example: "Group A", "Group B"

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterModel semester; // Division belongs to a specific semester

    // Constructors, getters, setters

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
