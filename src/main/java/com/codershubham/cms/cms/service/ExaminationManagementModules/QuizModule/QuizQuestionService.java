package com.codershubham.cms.cms.service.ExaminationManagementModules.QuizModule;

import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizModel;
import com.codershubham.cms.cms.model.ExaminationManagementModules.QuizModule.QuizQuestionModel;
import com.codershubham.cms.cms.repository.ExaminationManagementModules.QuizModule.QuizQuestionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class QuizQuestionService {

    @Autowired
    private QuizQuestionRepository questionRepository;

    @Autowired
    private QuizService quizService;

    // Add a question to a quiz
    public QuizQuestionModel addQuestion(Long quizId, String questionText, String option1, String option2, String option3, String option4, Integer correctOption) {
        // Fetch the quiz by its ID to ensure it exists
        QuizModel quiz = quizService.getQuizById(quizId);

        if (quiz == null) {
            throw new IllegalArgumentException("Quiz with ID " + quizId + " does not exist.");
        }

        // Create a new question and associate it with the quiz
        QuizQuestionModel question = new QuizQuestionModel();
        question.setQuiz(quiz);
        question.setQuestionText(questionText);
        question.setOption1(option1);
        question.setOption2(option2);
        question.setOption3(option3);
        question.setOption4(option4);
        question.setCorrectOption(correctOption);

        // Save and return the newly created question
        return questionRepository.save(question);
    }


    public void uploadQuestionsFromExcel(MultipartFile file, Long quizId) {
        try {
            // Get quiz by ID
            QuizModel quiz = quizService.getQuizById(quizId);
            if (quiz == null) {
                throw new IllegalArgumentException("Quiz with ID " + quizId + " does not exist.");
            }

            // Create workbook and sheet
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through rows
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                // Skip header row (optional, if needed)
                if (i == 0) {
                    continue; // Skip the first row if it's a header
                }

                try {
                    // Extract question text (ensure it's a valid string)
                    String questionText = convertCellToString(row, 0);
                    if (questionText == null || questionText.trim().isEmpty()) {
                        throw new IllegalArgumentException("Question text is missing or invalid at row " + i);
                    }

                    // Extract options (convert integers to strings)
                    String option1 = convertCellToString(row, 1);
                    String option2 = convertCellToString(row, 2);
                    String option3 = convertCellToString(row, 3);
                    String option4 = convertCellToString(row, 4);

                    // Handle correct option (ensure it's numeric)
                    int correctOption = getNumericCellValue(row, 5);

                    // Create and save the question
                    QuizQuestionModel question = new QuizQuestionModel();
                    question.setQuiz(quiz);
                    question.setQuestionText(questionText);
                    question.setOption1(option1);
                    question.setOption2(option2);
                    question.setOption3(option3);
                    question.setOption4(option4);
                    question.setCorrectOption(correctOption);

                    questionRepository.save(question);
                } catch (Exception e) {
                    // Log the error for the specific row
                    System.err.println("Error processing row " + i + ": " + e.getMessage());
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file read error
        }
    }

    // Helper method to convert cell value to string, regardless of type
    private String convertCellToString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();  // Return string directly
                case NUMERIC:
                    return String.valueOf((int) cell.getNumericCellValue()); // Convert numeric to string
                default:
                    return "";  // If the cell is empty or has an unsupported type, return empty string
            }
        }
        return "";  // If cell is null or invalid, return empty string
    }

    // Helper method to get the numeric value of the correct option
    private int getNumericCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue(); // Return numeric value as int
        } else {
            return -1;  // Return -1 if there's an issue with the cell value
        }
    }


    // Fetch questions by quiz ID
    public List<QuizQuestionModel> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public void updateQuestion(Long questionId, String questionText, String option1, String option2, String option3, String option4, Integer correctOption) {
        QuizQuestionModel question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question not found"));

        question.setQuestionText(questionText);
        question.setOption1(option1);
        question.setOption2(option2);
        question.setOption3(option3);
        question.setOption4(option4);
        question.setCorrectOption(correctOption);

        questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

}