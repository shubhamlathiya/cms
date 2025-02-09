package com.codershubham.cms.cms.service.StudentManagementModules;


import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.DTO.SubjectEnrollmentRequestDto;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SubjectEnrollmentModel;
import com.codershubham.cms.cms.repository.StudentManagementModules.SubjectEnrollmentRepository;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubjectEnrollmentService {

    @Autowired
    private SubjectEnrollmentRepository subjectEnrollmentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CourseService courseService;

    public void enrollStudentsInSubjects(SubjectEnrollmentRequestDto request) {
        Date enrollmentDate = (request.getEnrollmentDate() != null) ? request.getEnrollmentDate() : new Date();

        for (Long studentId : request.getStudentIds()) {
            StudentModel student = studentService.findById(studentId);
            CourseModel course = courseService.getCourseById(request.getCourseId());

            // ✅ Fetch SemesterModel using semesterId
            SemesterModel semester = semesterService.getSemesterById(request.getSemesterId());

            for (Long subjectId : request.getSubjectIds()) {
                SubjectsModel subject = subjectService.getSubjectById(subjectId);

                SubjectEnrollmentModel enrollment = new SubjectEnrollmentModel();
                enrollment.setStudent(student);
                enrollment.setSubject(subject);
                enrollment.setCourse(course);
                enrollment.setSemester(semester);  // ✅ Now we set SemesterModel instead of int
                enrollment.setEnrollmentDate(enrollmentDate);

                subjectEnrollmentRepository.save(enrollment);
            }
        }
    }

    // Get subjects enrolled by a student in a semester
    public List<SubjectEnrollmentModel> getStudentSubjects(Long studentId, Long semesterId) {
        return subjectEnrollmentRepository.findByStudentIdAndSemesterId(studentId, semesterId);
    }
}