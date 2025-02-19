package com.codershubham.cms.cms.model.DTO;

import java.util.ArrayList;
import java.util.List;

public class StudentQuestionsDto {
    private Long studentId;
    private String studentName;
    private List<String> questions;
    private String filePath;
    private boolean submitted;
    // Constructor
    public StudentQuestionsDto(Long studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.questions = new ArrayList<>();
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
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
