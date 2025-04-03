package com.codershubham.cms.cms.service.ExaminationManagementModules.QuizModule;

import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizAnswerModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizAttemptModel;
import com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule.QuizAnswerRepository;
import com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule.QuizAttemptRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizAttemptService {
    @Autowired
    private QuizAttemptRepository attemptRepository;

    @Autowired
    private QuizAnswerRepository answerRepository;

    @Transactional
    public void saveAttempt(QuizAttemptModel attempt, List<QuizAnswerModel> answers) {
        attemptRepository.save(attempt);
        for (QuizAnswerModel answer : answers) {
            answer.setAttempt(attempt);
        }
        answerRepository.saveAll(answers);
    }

    public Optional<QuizAttemptModel> getAttemptByStudentAndQuiz(Long studentId, Long quizId) {
        return attemptRepository.findByStudentIdAndQuizId(studentId, quizId);
    }
}
