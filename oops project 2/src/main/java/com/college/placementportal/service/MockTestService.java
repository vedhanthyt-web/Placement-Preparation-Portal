package com.college.placementportal.service;

import com.college.placementportal.exception.ResourceNotFoundException;
import com.college.placementportal.exception.ValidationException;
import com.college.placementportal.model.*;
import com.college.placementportal.repository.QuestionRepository;
import com.college.placementportal.repository.ResultRepository;
import com.college.placementportal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MockTestService {

    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;

    // Cache of active test sessions by testId
    private final Map<String, MockTest> activeTests = new ConcurrentHashMap<>();

    public MockTestService(QuestionRepository questionRepository,
                           ResultRepository resultRepository,
                           StudentRepository studentRepository) {
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Creates and starts a new timed mock test session.
     */
    public MockTest startMockTest(Long studentId, String category, int count, int durationMinutes) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        if (count <= 0) count = 5;
        if (durationMinutes <= 0) durationMinutes = 10;

        List<QuestionEntity> selectedEntities;
        if (category == null || category.equalsIgnoreCase("FULL_MOCK") || category.equalsIgnoreCase("ALL")) {
            selectedEntities = questionRepository.findRandomQuestions(count);
        } else {
            selectedEntities = questionRepository.findRandomQuestionsByCategory(category.trim(), count);
        }

        if (selectedEntities.isEmpty()) {
            throw new ValidationException("No questions available for the selected category: " + category);
        }

        List<Question> domainQuestions = new ArrayList<>();
        for (QuestionEntity entity : selectedEntities) {
            domainQuestions.add(entity.toDomainQuestion());
        }

        String testId = "TEST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String title = (category == null || category.equalsIgnoreCase("ALL")) ? "Full Mock Placement Assessment" : category + " Assessment Test";

        MockTest mockTest = new MockTest(testId, studentId, title, category, durationMinutes, domainQuestions);
        activeTests.put(testId, mockTest);

        return mockTest;
    }

    /**
     * Submits a mock test, grades it automatically using OOP polymorphism, and stores the Result.
     */
    public Map<String, Object> submitMockTest(String testId, Map<Long, String> studentAnswers) {
        MockTest mockTest = activeTests.get(testId);
        if (mockTest == null) {
            throw new ResourceNotFoundException("Active test session not found for ID: " + testId);
        }
        if (mockTest.isSubmitted()) {
            throw new ValidationException("Test has already been submitted.");
        }

        mockTest.setSubmitted(true);
        Student student = studentRepository.findById(mockTest.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for test"));

        int correctCount = 0;
        int wrongCount = 0;
        int unattemptedCount = 0;
        int scoreObtained = 0;
        int totalMarks = 0;

        List<Map<String, Object>> reviewList = new ArrayList<>();

        // Demonstrate Polymorphism: evaluate each question through the Question base reference
        for (Question question : mockTest.getQuestions()) {
            totalMarks += question.getMarks();
            String chosen = studentAnswers != null ? studentAnswers.get(question.getId()) : null;

            Map<String, Object> reviewItem = new HashMap<>();
            reviewItem.put("questionId", question.getId());
            reviewItem.put("questionText", question.getQuestionText());
            reviewItem.put("category", question.getCategory());
            reviewItem.put("selectedOption", chosen);
            reviewItem.put("correctOption", question.getCorrectOption());
            reviewItem.put("explanation", question.getExplanation());
            reviewItem.put("marks", question.getMarks());

            if (chosen == null || chosen.trim().isEmpty()) {
                unattemptedCount++;
                reviewItem.put("status", "UNATTEMPTED");
                reviewItem.put("isCorrect", false);
            } else {
                // Runtime Polymorphism: Question.evaluateAnswer dynamically calls the appropriate method
                boolean isCorrect = question.evaluateAnswer(chosen);
                if (isCorrect) {
                    correctCount++;
                    scoreObtained += question.getMarks();
                    reviewItem.put("status", "CORRECT");
                    reviewItem.put("isCorrect", true);
                } else {
                    wrongCount++;
                    reviewItem.put("status", "WRONG");
                    reviewItem.put("isCorrect", false);
                }
            }
            reviewList.add(reviewItem);
        }

        // Create Result entity (which implements Scorable)
        Result result = new Result(student.getId(), student.getName(), mockTest.getTitle(),
                mockTest.getCategory(), mockTest.getTotalQuestions(), correctCount,
                wrongCount, unattemptedCount, scoreObtained, totalMarks);

        Result savedResult = resultRepository.save(result);

        // Update student progress statistics using encapsulation method
        student.updatePerformance(savedResult.getPercentage());
        studentRepository.save(student);

        // Remove from active cache
        activeTests.remove(testId);

        Map<String, Object> response = new HashMap<>();
        response.put("resultId", savedResult.getId());
        response.put("testId", testId);
        response.put("studentName", student.getName());
        response.put("title", savedResult.getTestTitle());
        response.put("totalQuestions", savedResult.getTotalQuestions());
        response.put("correctAnswers", savedResult.getCorrectAnswers());
        response.put("wrongAnswers", savedResult.getWrongAnswers());
        response.put("unattempted", savedResult.getUnattempted());
        response.put("scoreObtained", savedResult.getScoreObtained());
        response.put("totalMarks", savedResult.getTotalMarks());
        response.put("percentage", savedResult.getPercentage());
        response.put("grade", savedResult.getGrade());
        response.put("studentUpdatedAvg", student.getAverageScore());
        response.put("studentTotalTests", student.getTestsAttempted());
        response.put("questionsReview", reviewList);

        return response;
    }
}
