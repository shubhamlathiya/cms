package com.codershubham.cms.cms.service.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.repository.StudentManagementModules.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    // Save the division
    public DivisionModel save(DivisionModel division) {
        return divisionRepository.save(division);
    }

//    public List<DivisionModel> findBySemesterId(Long semesterId) {
//        return divisionRepository.findBySemesterId(semesterId);
//    }

    public List<DivisionModel> getDivisionsBySemester(Long semesterId) {
        return divisionRepository.findBySemesterId(semesterId);
    }

    public DivisionModel getDivisionById(Long divisionId) {
        return divisionRepository.findById(divisionId)
                .orElseThrow(() -> new RuntimeException("Division not found with ID: " + divisionId));
    }
//    public List<DivisionModel> getAllDivisions() {
//        return divisionRepository.findAll();
//    }

}
