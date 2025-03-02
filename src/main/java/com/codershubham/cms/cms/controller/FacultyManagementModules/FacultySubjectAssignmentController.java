package com.codershubham.cms.cms.controller.FacultyManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
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
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(PathConstant.FACULTY_SUBJECT_PATH)
public class FacultySubjectAssignmentController {

    @Autowired
    private FacultySubjectAssignmentService facultySubjectAssignmentService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private SubjectService subjectService;


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
}
