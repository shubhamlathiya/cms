package com.codershubham.cms.cms.model.DTO;

import java.util.List;

public class AssignStudentsRequestDto {
    private List<Long> studentIds;
    private Long semesterId;
    private Long divisionId;

    // Getters and Setters
    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Long divisionId) {
        this.divisionId = divisionId;
    }
}
