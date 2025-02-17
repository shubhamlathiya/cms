package com.codershubham.cms.cms.model.DTO;

import java.util.List;

public class CreateDivisionsRequestDto {
    private Long semesterId;
    private List<String> divisionNames;
    private List<Long> facultyIds;

    public List<Long> getFacultyIds() {
        return facultyIds;
    }

    public void setFacultyIds(List<Long> facultyIds) {
        this.facultyIds = facultyIds;
    }

    // Getters and Setters
    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public List<String> getDivisionNames() {
        return divisionNames;
    }

    public void setDivisionNames(List<String> divisionNames) {
        this.divisionNames = divisionNames;
    }
}
