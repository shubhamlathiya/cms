package com.codershubham.cms.cms.controller.LeaveManagementModules;


import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.LeaveManagementModules.LeaveRequestModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.LeaveManagementModules.LeaveRequestService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.service.UserManagementModules.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final UserService userService;
    private final FacultyService facultyService;
    private final StudentService studentService;

    public LeaveRequestController(LeaveRequestService leaveRequestService, UserService userService, FacultyService facultyService, StudentService studentService) {
        this.leaveRequestService = leaveRequestService;
        this.userService = userService;
        this.facultyService = facultyService;
        this.studentService = studentService;
    }

    // ✅ Display Leave Request Page for a Specific User
    @GetMapping("/user/{userId}/view")
    public String showUserLeaveRequests(@PathVariable Long userId, Model model) {
        // Fetch user details
        UserModel user = userService.getUserById(userId);

        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // Fetch leave requests for the user
        List<LeaveRequestModel> leaveRequests = leaveRequestService.getLeaveRequestsByUser(userId);

        // Get user's role
        RoleModel role = user.getRole(); // Assuming there's a getRole() method in UserModel

        StudentModel student = null;
        FacultyModel faculty = null;

        if ("STUDENT".equalsIgnoreCase(role.getName())) {
            // Fetch student details if the user is a student
            student = studentService.getStudentByUserId(userId);
        } else if ("FACULTY".equalsIgnoreCase(role.getName())) {
            // Fetch faculty details if the user is a faculty member
            faculty = facultyService.getFacultyByUserId(userId);
        }

        // Add data to model
        model.addAttribute("leaveRequests", leaveRequests);
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        model.addAttribute("student", student); // Will be null if not a student
        model.addAttribute("faculty", faculty); // Will be null if not a faculty member

        return "LeaveManagement/leave-request"; // Returns the HTML page
    }

    // ✅ Submit a Leave Request (Student or Faculty)
    @PostMapping("/submit")
    public String submitLeaveRequest(@RequestParam Long userId,
                                     @RequestParam String reason,
                                     @RequestParam String startDate,
                                     @RequestParam String endDate,
                                     Model model) {
        LeaveRequestModel leaveRequest = new LeaveRequestModel();
        leaveRequest.setUser(userService.getUserById(userId));
        leaveRequest.setReason(reason);
        leaveRequest.setStartDate(java.time.LocalDate.parse(startDate));
        leaveRequest.setEndDate(java.time.LocalDate.parse(endDate));
        leaveRequest.setStatus("PENDING"); // Default status

        leaveRequestService.createLeaveRequest(userId, leaveRequest);

        return "redirect:/leave-requests/user/" + userId + "/view";
    }

    // ✅ Approve or Reject a Leave Request
    @PostMapping("/update")
    public String updateLeaveStatus(@RequestParam Long leaveId,
                                    @RequestParam String status,
                                    @RequestParam Long userId) {
        if (!status.equalsIgnoreCase("APPROVED") && !status.equalsIgnoreCase("REJECTED")) {
            return "redirect:/leave-requests/user/" + userId + "/view?error=Invalid Status";
        }

        leaveRequestService.updateLeaveStatus(leaveId, status);

        return "redirect:/leave-requests/user/" + userId + "/view";
    }
}
