package com.codershubham.cms.cms.controller.LeaveManagementModules;


import com.codershubham.cms.cms.model.LeaveManagementModules.LeaveRequestModel;
import com.codershubham.cms.cms.service.LeaveManagementModules.LeaveRequestService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/leave-requests")
@CrossOrigin(origins = "*")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    // Submit Leave Request (Student or Faculty)
    @PostMapping("/submit/{userId}")
    public ResponseEntity<LeaveRequestModel> submitLeaveRequest(@PathVariable Long userId, @RequestBody LeaveRequestModel leaveRequest) {
        LeaveRequestModel createdLeaveRequest = leaveRequestService.createLeaveRequest(userId, leaveRequest);
        return ResponseEntity.ok(createdLeaveRequest);
    }

    // Get Leave Requests by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveRequestModel>> getUserLeaveRequests(@PathVariable Long userId) {
        List<LeaveRequestModel> leaveRequests = leaveRequestService.getLeaveRequestsByUser(userId);
        return ResponseEntity.ok(leaveRequests);
    }

    // Approve or Reject Leave Request
    @PutMapping("/update/{leaveId}/{status}")
    public ResponseEntity<LeaveRequestModel> updateLeaveStatus(@PathVariable Long leaveId, @PathVariable String status) {
        if (!status.equalsIgnoreCase("APPROVED") && !status.equalsIgnoreCase("REJECTED")) {
            return ResponseEntity.badRequest().build();
        }
        LeaveRequestModel updatedLeaveRequest = leaveRequestService.updateLeaveStatus(leaveId, status);
        return ResponseEntity.ok(updatedLeaveRequest);
    }
}
