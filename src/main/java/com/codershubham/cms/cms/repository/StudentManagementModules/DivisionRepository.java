package com.codershubham.cms.cms.repository.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivisionRepository extends JpaRepository<DivisionModel, Long> {

// Custom query to find divisions by semester ID
    List<DivisionModel> findBySemesterId(Long semesterId);

    @Query("SELECT d.faculty.facultyId FROM DivisionModel d WHERE d.id = :divisionId")
    Long findFacultyIdByDivisionId(Long divisionId);
}
