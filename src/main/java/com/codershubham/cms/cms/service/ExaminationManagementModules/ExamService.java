package com.codershubham.cms.cms.service.ExaminationManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormStatus;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.repository.ExaminationManagementModules.ExamRepository;
import com.codershubham.cms.cms.repository.FacultyManagementModules.FacultyRepository;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private FacultyRepository FacultyRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ExamFormService examFormService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private FacultyService facultyService;

    @Transactional
    public boolean submitExamForm(Long studentId,Long facultyId, Long examFormId, List<Long> subjectIds, Double feeAmount) {
        try {
            // Fetch student, exam form, and subjects
            StudentModel student = studentService.findById(studentId);
            ExamFormModel examForm = examFormService.findById(examFormId);
            List<SubjectsModel> subjects = subjectService.findAllById(subjectIds);

            FacultyModel facultyModel = facultyService.findById(facultyId);
            // Create ExamFormDetails entry
            ExamModel examFormDetails = new ExamModel();
            examFormDetails.setStudent(student);
            examFormDetails.setExamForm(examForm);
            examFormDetails.setSubjects(subjects);
            examFormDetails.setApprovedByFaculty(facultyModel);
            examFormDetails.setFeeAmount(feeAmount);
            examFormDetails.setSubmissionDate(LocalDate.now());
            examFormDetails.setStatus(ExamFormStatus.PENDING_APPROVAL);
            examFormDetails.setPaymentStatus(false); // Payment not yet confirmed

            // Save the exam form submission
            examRepository.save(examFormDetails);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void facultyApproveExamForm(Long examFormDetailId, Long facultyId, boolean approve) {
        ExamModel details = examRepository.findById(examFormDetailId).orElseThrow();
        FacultyModel faculty = FacultyRepository.findById(facultyId).orElseThrow();

        if (approve) {
            details.setStatus(ExamFormStatus.FACULTY_APPROVED);
        } else {
            details.setStatus(ExamFormStatus.FACULTY_REJECTED);
        }

        details.setApprovedByFaculty(faculty);
        examRepository.save(details);
    }

    public boolean hasStudentSubmittedExamForm(Long studentId) {
        StudentModel student = studentService.findById(studentId);

        List<ExamModel> examForms = examRepository.findByStudent(student);
        return !examForms.isEmpty();  // If there are any records, the student has already submitted
    }

    public ExamModel getExamByStudentAndForm(Long studentId, Long examFormId) {
        return examRepository.findByStudentIdAndExamFormId(studentId, examFormId).orElse(null);
    }
}
