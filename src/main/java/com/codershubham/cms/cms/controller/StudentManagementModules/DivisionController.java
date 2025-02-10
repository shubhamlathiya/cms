package com.codershubham.cms.cms.controller.StudentManagementModules;


import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.DTO.CreateDivisionsRequestDto;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DivisionController {

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private DivisionService divisionService;


    @PostMapping("/create-divisions")
    public String createDivisions(@ModelAttribute CreateDivisionsRequestDto request, Model model) {

        System.out.println(request.getSemesterId());
        System.out.println(request.getDivisionNames());

        // Fetch the semester object by semesterId
        SemesterModel semester = semesterService.getSemesterById(request.getSemesterId());

        // Iterate over division names and save each one
        for (String divisionName : request.getDivisionNames()) {
            DivisionModel division = new DivisionModel();
            division.setName(divisionName);
            division.setSemester(semester);
            divisionService.save(division);
        }

        // Optionally, add success message to the model
        model.addAttribute("message", "Divisions created successfully!");

        return "redirect:/" + PathConstant.SEMESTER_PATH; // Redirect to semester view after saving
    }
}

