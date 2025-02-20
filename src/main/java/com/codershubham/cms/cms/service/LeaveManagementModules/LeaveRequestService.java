package com.codershubham.cms.cms.service.LeaveManagementModules;


import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.LeaveManagementModules.LeaveRequestModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.repository.FacultyManagementModules.FacultyRepository;
import com.codershubham.cms.cms.repository.LeaveManagementModules.LeaveRequestRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.DivisionRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.StudentEnrollmentRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.UserRepository;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final DivisionRepository divisionRepository;
    private final StudentService studentService;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, UserRepository userRepository, FacultyRepository facultyRepository, StudentEnrollmentRepository studentEnrollmentRepository, DivisionRepository divisionRepository, StudentService studentService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;

        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.divisionRepository = divisionRepository;
        this.studentService = studentService;
    }

    public LeaveRequestModel createLeaveRequest(Long userId, LeaveRequestModel leaveRequest) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        leaveRequest.setUser(user);
        leaveRequest.setStatus("PENDING");

        if (user.getRole().getName().equalsIgnoreCase("STUDENT")) {
            StudentModel student = studentService.getStudentByUserId(userId);
            Long divisionId = studentEnrollmentRepository.findDivisionIdByStudentId(student.getId());
            Long facultyId = divisionRepository.findFacultyIdByDivisionId(divisionId);
            FacultyModel faculty = facultyRepository.findById(facultyId).orElseThrow(() -> new RuntimeException("Faculty not found"));

            leaveRequest.setFaculty(faculty);
        }

        return leaveRequestRepository.save(leaveRequest);
    }

    // Get Leave Requests by User ID
    public List<LeaveRequestModel> getLeaveRequestsByUser(Long userId) {
        return leaveRequestRepository.findByUserId(userId);
    }

    // Approve or Reject Leave Request
    public LeaveRequestModel updateLeaveStatus(Long leaveId, String status) {
        LeaveRequestModel leaveRequest = leaveRequestRepository.findById(leaveId).orElseThrow(() -> new RuntimeException("Leave request not found"));
        leaveRequest.setStatus(status);
        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequestModel> getLeaveRequestsByFacultyId(Long facultyId) {
        // Retrieve all leave requests where the facultyId matches
        return leaveRequestRepository.findLeaveRequestsByFacultyId(facultyId);
    }

    public void updateLeaveRequest(LeaveRequestModel leaveRequest) {
        leaveRequestRepository.save(leaveRequest);
    }

    // ✅ Method to get Leave Request by ID
    public LeaveRequestModel getLeaveRequestById(Long leaveId) {
        Optional<LeaveRequestModel> leaveRequestOptional = leaveRequestRepository.findById(leaveId);

        // Return the LeaveRequest if found, or throw an exception if not found
        return leaveRequestOptional.orElseThrow(() -> new RuntimeException("Leave request not found with ID: " + leaveId));
    }
}
