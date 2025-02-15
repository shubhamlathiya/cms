package com.codershubham.cms.cms.model.DTO;

import java.util.ArrayList;
import java.util.List;

public class StudentQuestionsDTO {
    private Long studentId;
    private String studentName;
    private List<String> questions;

    // Constructor
    public StudentQuestionsDTO(Long studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.questions = new ArrayList<>();
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
