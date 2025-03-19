package com.codershubham.cms.cms.controller.ExaminationManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.ExamFormModel;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.service.ExaminationManagementModules.ExamFormService;
import com.codershubham.cms.cms.util.PdfGeneratorUtil;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(PathConstant.EXAM_PATH)
public class ExamController {

    @Autowired
    private ExamFormService examFormService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private HttpSession session;

    @GetMapping
    public String getAllExam(Model model) {
        model.addAttribute("exams", examFormService.getAllExamForms());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "ExaminationManagement/exam-details";  // Thymeleaf template 'courses.html'
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPdf() {
//        byte[] pdfBytes = PdfGeneratorUtil.generatePdf("Hello, this is a reusable PDF utility in Spring Boot!");
        List<String[]> tableData = Arrays.asList(
                new String[]{"ID", "Name", "Score"},
                new String[]{"1", "Alice", "85"},
                new String[]{"2", "Bob", "90"},
                new String[]{"3", "Charlie", "78"}
        );

        byte[] pdfBytes = PdfGeneratorUtil.generatePdfWithTable("Student Scores", tableData);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated.pdf").contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
    }

    // Show the exam form creation page
    @GetMapping(PathConstant.ADD_PATH)
    public String showCreateExamForm(Model model) {

        List<DepartmentModel> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        model.addAttribute("examForm", new ExamFormModel());
        return "ExaminationManagement/create-exam-form"; // Return the HTML template
    }

    // Handle exam form submission
    @PostMapping(PathConstant.ADD_PATH)
    public String createExamForm(ExamFormModel examForm) {
//        System.out.println(examForm.getDepartment());
        examFormService.createExamForm(examForm);
        return "redirect:/admin/exam"; // Redirect to the form page
    }

    @GetMapping(PathConstant.DELETE_PATH)
    public String deleteExams(@PathVariable Long id) {
        examFormService.deleteExam(id);
        return "redirect:/" + PathConstant.EXAM_PATH;
    }
}