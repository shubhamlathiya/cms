package com.codershubham.cms.cms.service.StudentManagementModules;

import com.codershubham.cms.cms.model.StudentManagementModules.*;
import com.codershubham.cms.cms.repository.StudentManagementModules.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Random;
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

    // Fetch assignments by division and semester
    public List<AssignmentModel> getAssignmentsByDivisionAndSemester(Long divisionId, Long semesterId) {
        return assignmentRepository.findByDivisionIdAndSemesterId(divisionId, semesterId);
    }

    // Create a new assignment
    public void createAssignment(Long divisionId, Long semesterId, Integer maxQuestions, String questions, boolean randomAssignment, MultipartFile file) {
        AssignmentModel assignment = new AssignmentModel();
        assignment.setDivisionId(divisionId);
        assignment.setSemesterId(semesterId);
        if (maxQuestions != null) {
            assignment.setMaxQuestions(maxQuestions);
        } else {
            List<String> questionList = List.of(questions.split("\n"));
            int max= questionList.size();
            assignment.setMaxQuestions(max);
        }

//        assignment.setMaxQuestions(maxQuestions);
        assignment.setFilePath(file != null ? file.getOriginalFilename() : null);
        assignmentRepository.save(assignment);

//        System.out.println(maxQuestions);
        if (questions != null && !questions.isEmpty()) {
//            System.out.println(maxQuestions);
            List<String> questionList = List.of(questions.split("\n"));
            Random random = new Random();

            // Fetch students enrolled in the division and semester
            List<StudentEnrollmentModel> enrollments = studentEnrollmentRepository.findByDivisionIdAndSemesterId(divisionId, semesterId);

            for (StudentEnrollmentModel enrollment : enrollments) {
                List<String> assignedQuestions;
                if (randomAssignment) {
                    // Assign random questions
                    assignedQuestions = questionList.stream().sorted((q1, q2) -> random.nextInt() - random.nextInt()).limit(maxQuestions != null ? maxQuestions : questionList.size()).collect(Collectors.toList());
                } else {
                    // Assign all questions
                    assignedQuestions = questionList;
                }

                for (String questionText : assignedQuestions) {
                    QuestionModel question = new QuestionModel();
                    question.setAssignment(assignment); // Set the AssignmentModel object
                    question.setQuestionText(questionText);
                    questionRepository.save(question);

                    StudentAssignmentModel studentAssignment = new StudentAssignmentModel();
                    studentAssignment.setAssignment(assignment); // Correct: Set the AssignmentModel object
                    studentAssignment.setStudent(enrollment.getStudent()); // Set the StudentModel object
                    studentAssignment.setQuestion(question); // Set the QuestionModel object
                    studentAssignmentRepository.save(studentAssignment);
                }
            }
        }

        // Handle file upload if present
        if (file != null && !file.isEmpty()) {
            // Save the file to a specific location or process it as needed
        }
    }


    // Fetch list of students and their assigned questions for a specific assignment
//    public List<StudentAssignmentModel> getAssignedQuestions(Long assignmentId) {
//        return studentAssignmentRepository.findByAssignmentId(assignmentId);
//    }
    public List<StudentAssignmentModel> getAssignedQuestions(Long assignmentId) {
        return studentAssignmentRepository.findByAssignmentId(assignmentId);
    }

}