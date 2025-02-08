package com.codershubham.cms.cms.repository.LeaveManagementModules;


import com.codershubham.cms.cms.model.LeaveManagementModules.LeaveRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestModel, Long> {
    List<LeaveRequestModel> findByUserId(Long userId);
}
