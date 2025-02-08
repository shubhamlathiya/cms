package com.codershubham.cms.cms.model.DTO;

import java.util.List;

public class CreateDivisionsRequestDto {
    private Long semesterId;
    private List<String> divisionNames;

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
