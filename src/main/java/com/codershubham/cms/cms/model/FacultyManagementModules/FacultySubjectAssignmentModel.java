package com.codershubham.cms.cms.model.FacultyManagementModules;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import jakarta.persistence.*;

@Entity
@Table(name = "faculty_subject_assignment")
public class FacultySubjectAssignmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyModel faculty;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private SemesterModel semester;

    @ManyToOne
    @JoinColumn(name = "division_id", nullable = false)
    private DivisionModel division;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectsModel subject;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FacultyModel getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyModel faculty) {
        this.faculty = faculty;
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

    public SubjectsModel getSubject() {
        return subject;
    }

    public void setSubject(SubjectsModel subject) {
        this.subject = subject;
    }
}
