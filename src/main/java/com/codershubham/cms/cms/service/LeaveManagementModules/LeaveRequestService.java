package com.codershubham.cms.cms.service.LeaveManagementModules;


import com.codershubham.cms.cms.model.LeaveManagementModules.LeaveRequestModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.repository.LeaveManagementModules.LeaveRequestRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, UserRepository userRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
    }

    // Create Leave Request
    public LeaveRequestModel createLeaveRequest(Long userId, LeaveRequestModel leaveRequest) {
        Optional<UserModel> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        leaveRequest.setUser(userOptional.get());
        leaveRequest.setStatus("PENDING"); // Default status
        return leaveRequestRepository.save(leaveRequest);
    }

    // Get Leave Requests by User ID
    public List<LeaveRequestModel> getLeaveRequestsByUser(Long userId) {
        return leaveRequestRepository.findByUserId(userId);
    }

    // Approve or Reject Leave Request
    public LeaveRequestModel updateLeaveStatus(Long leaveId, String status) {
        LeaveRequestModel leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        leaveRequest.setStatus(status);
        return leaveRequestRepository.save(leaveRequest);
    }
}
