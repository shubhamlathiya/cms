package com.codershubham.cms.cms.controller.FacultyManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.model.DTO.FacultySubjectAssignmentRequestDto;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.CourseManagementModules.DepartmentService;
import com.codershubham.cms.cms.service.CourseManagementModules.SubjectService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultySubjectAssignmentService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/faculty-subject")
public class FacultySubjectAssignmentController {

    @Autowired
    private FacultySubjectAssignmentService facultySubjectAssignmentService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/assign-subject")
    public String showAssignmentPage(Model model) {
        model.addAttribute("faculties", facultyService.getAllFaculties());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "FacultyManagement/faculty/assign_faculty_subjects"; // Loads faculty_assignment.html
    }

    @PostMapping("/assign-subject")
    @ResponseBody
    public String assignSubjectToFaculty(@RequestBody FacultySubjectAssignmentRequestDto requestDto) {
        return facultySubjectAssignmentService.assignSubjectToFaculty(requestDto);
    }

    @GetMapping("/courses/{departmentId}")
    @ResponseBody
    public List<CourseModel> getCoursesByDepartment(@PathVariable Long departmentId) {
        return courseService.getCoursesByDepartmentId(departmentId);
    }

    @GetMapping("/semesters/{courseId}")
    public ResponseEntity<List<SemesterModel>> getSemestersByCourse(@PathVariable Long courseId) {
        List<SemesterModel> semesters = semesterService.getSemestersByCourse(courseId);
        return ResponseEntity.ok(semesters);
    }

    // Fetch faculty based on department
    @GetMapping("/faculty/{departmentId}")
    public ResponseEntity<List<FacultyModel>> getFacultyByDepartment(@PathVariable Long departmentId) {
        List<FacultyModel> facultyList = facultyService.getFacultyByDepartment(departmentId);
        return ResponseEntity.ok(facultyList);
    }


    @GetMapping("/subjects/{courseId}")
    public ResponseEntity<List<SubjectsModel>> getSubjectsByCourse(@PathVariable Long courseId) {
        List<SubjectsModel> subjects = subjectService.getSubjectsByCourseId(courseId);
        return ResponseEntity.ok(subjects);
    }
    // Fetch divisions by semester ID
    @GetMapping("/divisions/{semesterId}")
    public ResponseEntity<List<DivisionModel>> getDivisionsBySemester(@PathVariable Long semesterId) {
        List<DivisionModel> divisions = divisionService.getDivisionsBySemester(semesterId);
        return ResponseEntity.ok(divisions);
    }

//    // Handle the form submission
//    @PostMapping("/assign")
//    public String assignSubjectToFaculty(@RequestParam Long facultyId,
//                                         @RequestParam Long semesterId,
//                                         @RequestParam Long divisionId,
//                                         @RequestParam Long subjectId,
//                                         Model model) {
//        // Call service to assign subject
//        String result = facultySubjectAssignmentService.assignSubjectToFaculty(
//                new FacultySubjectAssignmentRequestDto(facultyId, semesterId, divisionId, subjectId));
//
//        // Pass the result message to the view
//        model.addAttribute("message", result);
//        return "faculty_assignment"; // Reload the same page with the message
//    }
}
