package com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule;

import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_attempts")
public class QuizAttemptModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentModel student;

    @Column(name = "attempted", nullable = false)
    private boolean attempted = false;  // Default to false if not set explicitly

    public boolean isAttempted() {
        return attempted;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizModel quiz;

    private int score;
    private LocalDateTime attemptedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL)
    private List<QuizAnswerModel> answers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }

    public QuizModel getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizModel quiz) {
        this.quiz = quiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(LocalDateTime attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    public List<QuizAnswerModel> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswerModel> answers) {
        this.answers = answers;
    }
}
