package com.codershubham.cms.cms.controller.StudentManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.service.CourseManagementModules.CourseService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.StudentManagementModules.DivisionService;
import com.codershubham.cms.cms.service.StudentManagementModules.SemesterService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentEnrollmentService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(PathConstant.SEMESTER_PATH)
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private UserRoleUtil userRoleUtil;

    @Autowired
    private HttpSession session;

    // Show the form to create a semester for a course
    @GetMapping(PathConstant.ADD_PATH)
    public String showCreateSemesterForm(Model model) {
        model.addAttribute("semester", new SemesterModel());
        model.addAttribute("courses", courseService.getAllCourses()); // All available courses

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/semester/create-semester"; // A view to create a semester
    }

    // Handle the form submission to create a semester for the selected course
    @PostMapping(PathConstant.ADD_PATH)
    public String createSemester(@ModelAttribute SemesterModel semester, @RequestParam Long courseId, Model model) {
        CourseModel course = courseService.getCourseById(courseId); // Fetch the selected course
        semester.setCourse(course); // Link the semester to the course

        SemesterModel createdSemester = semesterService.createSemester(semester); // Save the semester
        model.addAttribute("semester", createdSemester);
        return "redirect:/" + PathConstant.SEMESTER_PATH;  // Show success page after creating the semester
    }

    @GetMapping
    public String listSemesters(Model model) {
        List<SemesterModel> semesters = semesterService.findAll();

        List<Map<String, Object>> semesterDetails = semesters.stream().map(semester -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", semester.getId());
            map.put("name", semester.getName());
            map.put("academicYear", semester.getAcademicYear());
            map.put("startDate", semester.getStartDate());
            map.put("endDate", semester.getEndDate());

            // Fetch courseName using the CourseModel relation
            String courseName = (semester.getCourse() != null) ? semester.getCourse().getCourseName() : "N/A";
            map.put("courseName", courseName);

            return map;
        }).collect(Collectors.toList());

        model.addAttribute("semesterDetails", semesterDetails);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/semester/semester-list"; // Updated HTML page
    }

    @GetMapping("/create-division-page")
    public String showCreateDivisionPage(@RequestParam Long semesterId, Model model) {
        // Fetch existing divisions for the given semester
        List<DivisionModel> divisions = divisionService.getDivisionsBySemester(semesterId);

        // Fetch the semester object by semesterId
        SemesterModel semester = semesterService.getSemesterById(semesterId);

        // Fetch the course associated with the semester
        CourseModel course = semester.getCourse();

        // Fetch faculties belonging to the department of the course
        List<FacultyModel> departmentFaculties = facultyService.getFacultyByDepartment(course.getDepartment().getId());

        model.addAttribute("semesterId", semesterId);
        model.addAttribute("divisions", divisions); // Pass divisions list to the view
        model.addAttribute("departmentFaculties", departmentFaculties); // Pass department faculties to the view

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "StudentManagement/semester/create-division"; // Returns the division creation view
    }
}
