package com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_answers")
public class QuizAnswerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attempt_id", nullable = false)
    private QuizAttemptModel attempt;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestionModel question;

    private int selectedOption;
    private boolean isCorrect;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizAttemptModel getAttempt() {
        return attempt;
    }

    public void setAttempt(QuizAttemptModel attempt) {
        this.attempt = attempt;
    }

    public QuizQuestionModel getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestionModel question) {
        this.question = question;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
