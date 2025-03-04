package com.codershubham.cms.cms.service.ExaminationManagementModules;


import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.CourseRepository;
import com.codershubham.cms.cms.repository.ExaminationManagementModules.ExamFormRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.SemesterRepository;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamFormService {

    @Autowired
    private ExamFormRepository examFormRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentService departmentRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    public ExamFormModel createExamForm(ExamFormModel examForm) {
        DepartmentModel department = departmentRepository.getDepartmentById(examForm.getDepartment().getId());
        examForm.setDepartment(department);

        CourseModel course = courseRepository.findById(examForm.getCourse().getCourseID())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        examForm.setCourse(course);

        SemesterModel semester = semesterRepository.findById(examForm.getSemester().getId())
                .orElseThrow(() -> new RuntimeException("Semester not found"));
        examForm.setSemester(semester);

        return examFormRepository.save(examForm);
    }

    public ExamFormModel findById(Long examFormId) {
        return examFormRepository.findById(examFormId).orElseThrow(() -> new IllegalArgumentException("ExamForm not found with ID: " + examFormId));
    }


    public List<ExamFormModel> getAllExamForms() {
        return examFormRepository.findAll();
    }
    /**
     * Get the exam form for a given course and semester.
     * @param courseId The course ID.
     * @param semesterId The semester ID.
     * @return The exam form if found, otherwise null.
     */
    public ExamFormModel getExamFormByCourseAndSemester(Long courseId, Long semesterId) {
        return examFormRepository.findByCourseIdAndSemesterId(courseId, semesterId);
    }

    public void deleteExam(Long id) {
        examFormRepository.deleteById(id);
    }

}