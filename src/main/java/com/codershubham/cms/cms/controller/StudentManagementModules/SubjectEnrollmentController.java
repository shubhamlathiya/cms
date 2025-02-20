package com.codershubham.cms.cms.controller.StudentManagementModules;


import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.DTO.SubjectEnrollmentRequestDto;
import com.codershubham.cms.cms.model.StudentManagementModules.SubjectEnrollmentModel;
import com.codershubham.cms.cms.service.StudentManagementModules.SubjectEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(PathConstant.SUBJECT_ENROLLMENTS_PATH)
public class SubjectEnrollmentController {

    @Autowired
    private SubjectEnrollmentService subjectEnrollmentService;

    @PostMapping("/enroll")
    public String enrollStudentsInSubjects(@RequestBody SubjectEnrollmentRequestDto request, Model model) {
        subjectEnrollmentService.enrollStudentsInSubjects(request);
        model.addAttribute("message", "Students successfully enrolled in selected subjects!");
        return "redirect:/students";
    }

    //    // Get subjects for a student in a semester
    @GetMapping("/student/{studentId}/semester/{semesterId}")
    public List<SubjectEnrollmentModel> getStudentSubjects(@PathVariable Long studentId, @PathVariable Long semesterId) {
        return subjectEnrollmentService.getStudentSubjects(studentId, semesterId);
    }
}