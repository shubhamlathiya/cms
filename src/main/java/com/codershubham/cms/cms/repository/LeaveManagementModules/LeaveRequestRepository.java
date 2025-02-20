package com.codershubham.cms.cms.repository.LeaveManagementModules;


import com.codershubham.cms.cms.model.LeaveManagementModules.LeaveRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestModel, Long> {
    List<LeaveRequestModel> findByUserId(Long userId);

    //    List<LeaveRequestModel> findByFacultyId(Long facultyId);
    @Query("SELECT l FROM LeaveRequestModel l WHERE l.faculty.facultyId = :facultyId")
    List<LeaveRequestModel> findLeaveRequestsByFacultyId(@Param("facultyId") Long facultyId);

}
