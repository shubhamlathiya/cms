package com.codershubham.cms.cms.service.FacultyManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.DTO.FacultySubjectAssignmentRequestDto;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultySubjectAssignmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.SubjectRepository;
import com.codershubham.cms.cms.repository.FacultyManagementModules.FacultyRepository;
import com.codershubham.cms.cms.repository.FacultyManagementModules.FacultySubjectAssignmentRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.DivisionRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FacultySubjectAssignmentService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private FacultySubjectAssignmentRepository facultySubjectAssignmentRepository;

    public String assignSubjectToFaculty(FacultySubjectAssignmentRequestDto requestDto) {
        FacultyModel faculty = facultyRepository.findById(requestDto.getFacultyId()).orElseThrow(() -> new RuntimeException("Faculty not found"));

        SemesterModel semester = semesterRepository.findById(requestDto.getSemesterId()).orElseThrow(() -> new RuntimeException("Semester not found"));

        SubjectsModel subject = subjectRepository.findById(requestDto.getSubjectId()).orElseThrow(() -> new RuntimeException("Subject not found"));

        DivisionModel division = divisionRepository.findById(requestDto.getDivisionId()).orElseThrow(() -> new RuntimeException("Division not found"));

        if (facultySubjectAssignmentRepository.existsBySubjectAndDivision(subject, division)) {
            return "Error: This subject is already assigned to another faculty in this division.";
        }


        FacultySubjectAssignmentModel assignment = new FacultySubjectAssignmentModel();
        assignment.setFaculty(faculty);
        assignment.setSemester(semester);
        assignment.setDivision(division);
        assignment.setSubject(subject);

        facultySubjectAssignmentRepository.save(assignment);
        return "Subject assigned successfully!";
    }

    public FacultySubjectAssignmentModel findById(Long id) {
        return facultySubjectAssignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Faculty Subject not found with ID: " + id));
    }

    // ✅ Ensure this method exists
// Get subjects assigned to a faculty
    public List<FacultySubjectAssignmentModel> getSubjectsByFaculty(Long facultyId) {
        FacultyModel faculty = facultyRepository.findById(facultyId).orElseThrow(() -> new IllegalArgumentException("Faculty not found with ID: " + facultyId));

        return facultySubjectAssignmentRepository.findByFaculty(faculty);
    }

    public FacultySubjectAssignmentModel getAssignmentById(Long assignmentId) {
        return facultySubjectAssignmentRepository.findById(assignmentId).orElse(null); // Returns null if not found
    }
}
