package com.codershubham.cms.cms.controller.CourseManagementModules;


import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SyllabusModel;
import com.codershubham.cms.cms.service.CourseManagementModules.SyllabusService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(PathConstant.SYLLABUS_PATH)
public class SyllabusController {

    @Autowired
    private SyllabusService syllabusService;

    @Autowired
    HttpSession session;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @GetMapping("/subject/{subjectId}")
    public String getSyllabusBySubject(@PathVariable Long subjectId, Model model) {
        List<SyllabusModel> syllabusList = syllabusService.getSyllabusBySubject(subjectId);
        model.addAttribute("syllabusList", syllabusList);
        model.addAttribute("subjectId", subjectId);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "CourseManagement/syllabus/syllabus-by-subject"; // Returns syllabus-by-subject.html
    }


    @GetMapping("/add/{subjectId}")
    public String showAddSyllabusForm(@PathVariable Long subjectId, Model model) {
        SyllabusModel syllabus = new SyllabusModel();
        SubjectsModel subject = new SubjectsModel();
        subject.setSubjectid(subjectId);
        syllabus.setSubject(subject);

        model.addAttribute("syllabus", syllabus);
        model.addAttribute("subjectId", subjectId);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "CourseManagement/syllabus/syllabus-form"; // Navigate to syllabus form
    }

    // 2️⃣ Show Add Syllabus Form
    @GetMapping(PathConstant.ADD_PATH)
    public String showAddSyllabusForm(Model model) {
        model.addAttribute("syllabus", new SyllabusModel());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "CourseManagement/syllabus/syllabus-form";  // Returns syllabus-form.html
    }

    // 3️⃣ Process Add Syllabus Form
    @PostMapping(PathConstant.ADD_PATH)
    public String createSyllabus(@ModelAttribute SyllabusModel syllabus) {
        syllabusService.addSyllabus(syllabus);
        return "redirect:/" + PathConstant.SYLLABUS_PATH + "/subject/" + syllabus.getSubject().getSubjectid();
        // Redirect to syllabus list of that subject
    }

    // 4️⃣ Show Update Syllabus Form
    @GetMapping("/update/{id}")
    public String showUpdateSyllabusForm(@PathVariable Long id, Model model) {
        Optional<SyllabusModel> syllabus = syllabusService.getSyllabusById(id);
        if (syllabus.isPresent()) {
            model.addAttribute("syllabus", syllabus.get());
            model.addAttribute("subjectId", syllabus.get().getSubject().getSubjectid());

            String userRole = userRoleUtil.getUserRole(session);
            model.addAttribute("userRole", userRole);

            return "CourseManagement/syllabus/syllabus-form"; // Navigate to syllabus form
        } else {
            return "redirect:/" + PathConstant.SYLLABUS_PATH;  // Redirect to main syllabus list if not found
        }
    }

    // 5️⃣ Process Update Syllabus
    @PostMapping(PathConstant.UPDATE_PATH)
    public String updateSyllabus(@PathVariable Long id, @ModelAttribute SyllabusModel syllabus) {
        syllabusService.updateSyllabus(id, syllabus);
        return "redirect:/" + PathConstant.SYLLABUS_PATH + "/subject/" + syllabus.getSubject().getSubjectid();
        // Redirect to syllabus list of that subject after update
    }


    @GetMapping(PathConstant.DELETE_PATH)
    public String deleteSyllabus(@PathVariable Long id) {
        Optional<SyllabusModel> syllabus = syllabusService.getSyllabusById(id);
        if (syllabus.isPresent()) {
            Long subjectId = syllabus.get().getSubject().getSubjectid();
            syllabusService.deleteSyllabus(id);
            return "redirect:/syllabus/subject/" + subjectId;
            // Redirect to syllabus list of the respective subject after deletion
        }
        return "redirect:/" + PathConstant.SYLLABUS_PATH;   // Redirect to syllabus home if not found
    }
}
