package com.codershubham.cms.cms.service.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.*;
import com.codershubham.cms.cms.repository.StudentManagementModules.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @Autowired
    private StudentAssignmentRepository studentAssignmentRepository;

    @Autowired
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Autowired
    private StudentRepository studentRepository;


    public List<AssignmentModel> getAssignmentsByDivisionSemesterAndSubject(Long divisionId, Long semesterId, Long subjectId) {
        return assignmentRepository.findByDivisionIdAndSemesterIdAndSubjectId(divisionId, semesterId, subjectId);
    }

    public boolean hasAssignmentsForSubjectAndDivision(Long subjectId, Long divisionId) {
        return assignmentRepository.existsBySubjectIdAndDivisionId(subjectId, divisionId);
    }

    public List<QuestionModel> getQuestionsByAssignment(Long assignmentId) {
        return questionRepository.findByAssignmentId(assignmentId);
    }

    public List<StudentAssignmentModel> getAssignmentsByStudentAndSubject(Long studentId, Long subjectId) {
        return studentAssignmentRepository.findByStudentIdAndSubjectId(studentId, subjectId);
    }

    public void createAssignment(Long divisionId, Long semesterId, Long subjectId, String name, LocalDateTime deadline,
                                 int maxMarks, MultipartFile material, Integer maxQuestions, String questions,
                                 boolean randomAssignment, MultipartFile file) {

        AssignmentModel assignment = new AssignmentModel();
        assignment.setDivisionId(divisionId);
        assignment.setSemesterId(semesterId);
        assignment.setSubjectId(subjectId);
        assignment.setAssignmentName(name);
        assignment.setDeadLine(deadline);
        assignment.setMaxMarks(maxMarks);
        assignment.setMaxQuestions(maxQuestions != null ? maxQuestions : questions.split("\n").length);
        assignment.setFilePath(file != null ? file.getOriginalFilename() : null);

        assignmentRepository.save(assignment);

        // Handle question assignments
        if (questions != null && !questions.isEmpty()) {
            List<String> questionList = List.of(questions.split("\n"));
            Random random = new Random();

            List<StudentEnrollmentModel> enrollments = studentEnrollmentRepository.findByDivisionIdAndSemesterId(divisionId, semesterId);

            for (StudentEnrollmentModel enrollment : enrollments) {
                List<String> assignedQuestions = randomAssignment ?
                        questionList.stream().sorted((q1, q2) -> random.nextInt() - random.nextInt())
                                .limit(maxQuestions != null ? maxQuestions : questionList.size())
                                .collect(Collectors.toList()) : questionList;

                for (String questionText : assignedQuestions) {
                    QuestionModel question = new QuestionModel();
                    question.setAssignment(assignment);
                    question.setQuestionText(questionText);
                    questionRepository.save(question);

                    StudentAssignmentModel studentAssignment = new StudentAssignmentModel();
                    studentAssignment.setAssignment(assignment);
                    studentAssignment.setStudent(enrollment.getStudent());
                    studentAssignment.setQuestion(question);
                    studentAssignmentRepository.save(studentAssignment);
                }
            }
        }
    }

    public boolean hasStudentSubmittedAssignment(Long assignmentId, Long studentId) {
        // Assuming AssignmentSubmission is the entity that tracks submissions
        AssignmentSubmissionModel submission = assignmentSubmissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId);
        return submission != null; // If the submission exists, return true, else false
    }

    public void saveAssignmentSubmission(Long assignmentId, Long studentId, String filePath,
                                         int obtainedMarks, String remarks) {

        // Retrieve the assignment and student from the database
        AssignmentModel assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        StudentModel student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Create the submission object and set its properties
        AssignmentSubmissionModel submission = new AssignmentSubmissionModel();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmissionFilePath(filePath);
        submission.setObtainedMarks(obtainedMarks); // Marks are saved regardless of file
        submission.setRemarks(remarks); // Remarks are saved regardless of file

        // Save the submission to the database
        assignmentSubmissionRepository.save(submission);
    }

    public AssignmentSubmissionModel getAssignmentSubmission(Long assignmentId, Long studentId) {
        return assignmentSubmissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId);
    }

    public List<AssignmentSubmissionModel> getSubmissionsByAssignment(Long assignmentId) {
        return assignmentSubmissionRepository.findByAssignmentId(assignmentId);
    }

    public List<StudentAssignmentModel> getAssignedQuestions(Long assignmentId) {
        return studentAssignmentRepository.findByAssignmentId(assignmentId);
    }
}
