package com.codershubham.cms.cms.controller.StudentManagementModules;


import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.DTO.SubjectEnrollmentRequestDto;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SubjectEnrollmentModel;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.service.StudentManagementModules.SubjectEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subject-enrollment")
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
//    @GetMapping("/student/{studentId}/semester/{semesterId}")
//    public List<SubjectEnrollmentModel> getStudentSubjects(@PathVariable Long studentId, @PathVariable Long semesterId) {
//        return subjectEnrollmentService.getStudentSubjects(studentId, semesterId);
//    }
}