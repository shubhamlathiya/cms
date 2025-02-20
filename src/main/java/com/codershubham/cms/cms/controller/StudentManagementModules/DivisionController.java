package com.codershubham.cms.cms.controller.StudentManagementModules;


import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.DTO.CreateDivisionsRequestDto;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DivisionController {

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private FacultyService facultyService;

    @PostMapping("/create-divisions")
    public String createDivisions(@ModelAttribute CreateDivisionsRequestDto request, Model model) {
        System.out.println(request.getSemesterId());
        System.out.println(request.getDivisionNames());

        // Fetch the semester object by semesterId
        SemesterModel semester = semesterService.getSemesterById(request.getSemesterId());

        // Fetch the course associated with the semester
        CourseModel course = semester.getCourse();

        // Fetch faculties belonging to the department of the course
        List<FacultyModel> departmentFaculties = facultyService.getFacultyByDepartment(course.getDepartment().getId());

        // Validate that the number of divisions matches the number of faculty IDs
        if (request.getDivisionNames().size() != request.getFacultyIds().size()) {
            throw new IllegalArgumentException("Number of divisions and faculty IDs must match.");
        }

        // Iterate over division names and faculty IDs, and save each division
        for (int i = 0; i < request.getDivisionNames().size(); i++) {
            String divisionName = request.getDivisionNames().get(i);
            Long facultyId = request.getFacultyIds().get(i);

            // Fetch the faculty object by facultyId
            FacultyModel faculty = departmentFaculties.stream()
                    .filter(f -> f.getFacultyId().equals(facultyId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Faculty not found in the department"));

            // Create and save the division
            DivisionModel division = new DivisionModel();
            division.setName(divisionName);
            division.setSemester(semester);
            division.setFaculty(faculty); // Assign the faculty to the division
            divisionService.save(division);
        }

        // Optionally, add success message to the model
        model.addAttribute("message", "Divisions created successfully!");

        return "redirect:/" + PathConstant.SEMESTER_PATH; // Redirect to semester view after saving
    }
}

