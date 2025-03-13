package com.codershubham.cms.cms.controller.FacultyManagementModules;

import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormStatus;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.repository.ExaminationManagementModules.ExamRepository;
import com.codershubham.cms.cms.repository.FacultyManagementModules.FacultyRepository;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/faculty/exams")
public class FacultyExamController {

    @Autowired
    HttpSession session;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private FacultyService facultyService;

    private final ExamRepository examRepository;
    private final FacultyRepository facultyRepository;

    public FacultyExamController(ExamRepository examRepository, FacultyRepository facultyRepository) {
        this.examRepository = examRepository;
        this.facultyRepository = facultyRepository;
    }

    /**
     * Get all exam forms assigned to a faculty member and display in a view.
     */
    @GetMapping("/{facultyId}")
    public String getExamsForFaculty(@PathVariable Long facultyId, Model model) {
        List<ExamModel> exams = examRepository.findAll()
                .stream()
                .filter(exam -> exam.getApprovedByFaculty() != null && exam.getApprovedByFaculty().getFacultyId().equals(facultyId))
                .toList();

        model.addAttribute("facultyId", facultyId);
        model.addAttribute("exams", exams);

        Long userId = (Long) session.getAttribute("userId");
        FacultyModel faculty = facultyService.getFacultyByUserId(userId);
        model.addAttribute("faculty", faculty);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        return "FacultyManagement/faculty-exams-form-students";  // Redirects to list.html in templates/faculty/exams/
    }

    /**
     * Faculty approves or rejects an exam form.
     */
    @PostMapping("/{facultyId}/approve/{examFormId}")
    public String facultyApproveExamForm(
            @PathVariable Long facultyId,
            @PathVariable Long examFormId,
            @RequestParam boolean approve,
            Model model) {

        Optional<ExamModel> optionalExam = examRepository.findById(examFormId);
        Optional<FacultyModel> optionalFaculty = facultyRepository.findById(facultyId);

        if (optionalExam.isEmpty() || optionalFaculty.isEmpty()) {
            model.addAttribute("error", "Exam form or faculty not found");
            return "FacultyManagement/faculty-exams-form-students";
        }

        ExamModel exam = optionalExam.get();
        FacultyModel faculty = optionalFaculty.get();

        exam.setApprovedByFaculty(faculty);
        if (approve) {
            exam.setStatus(ExamFormStatus.FACULTY_APPROVED);
        } else {
            exam.setStatus(ExamFormStatus.FACULTY_REJECTED);
        }

        examRepository.save(exam);

        return "redirect:/faculty/exams/" + facultyId;
    }

    /**
     * Get all subjects included in an exam form.
     */
    @GetMapping("/subjects/{examFormId}")
    @ResponseBody  // This ensures JSON response instead of rendering a Thymeleaf view
    public ResponseEntity<?> getSubjectsForExam(@PathVariable Long examFormId) {
        Optional<ExamModel> optionalExam = examRepository.findById(examFormId);

        if (optionalExam.isEmpty()) {
            return ResponseEntity.badRequest().body("Exam form not found");
        }

        ExamModel exam = optionalExam.get();
        return ResponseEntity.ok(exam.getSubjects()); // Returns subjects as JSON
    }

}
