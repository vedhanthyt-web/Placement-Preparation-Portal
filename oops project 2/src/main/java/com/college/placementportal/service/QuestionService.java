package com.college.placementportal.service;

import com.college.placementportal.exception.ResourceNotFoundException;
import com.college.placementportal.exception.ValidationException;
import com.college.placementportal.model.*;
import com.college.placementportal.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Retrieves all questions stored in the database.
     */
    public List<QuestionEntity> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Retrieves polymorphic Question domain objects by category for practice.
     */
    public List<Question> getPracticeQuestions(String category) {
        List<QuestionEntity> entities;
        if (category == null || category.equalsIgnoreCase("ALL")) {
            entities = questionRepository.findAll();
        } else {
            entities = questionRepository.findByCategoryIgnoreCase(category.trim());
        }

        List<Question> domainQuestions = new ArrayList<>();
        for (QuestionEntity entity : entities) {
            domainQuestions.add(entity.toDomainQuestion());
        }
        return domainQuestions;
    }

    /**
     * Evaluates a practice question answer using OOP polymorphism.
     */
    public Map<String, Object> evaluatePracticeAnswer(Long questionId, String selectedOption) {
        QuestionEntity entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        // Use polymorphic Question object to evaluate
        Question question = entity.toDomainQuestion();
        boolean isCorrect = question.evaluateAnswer(selectedOption);

        Map<String, Object> response = new HashMap<>();
        response.put("questionId", questionId);
        response.put("selectedOption", selectedOption != null ? selectedOption.toUpperCase() : "");
        response.put("correctOption", entity.getCorrectOption());
        response.put("isCorrect", isCorrect);
        response.put("explanation", entity.getExplanation());
        response.put("marksAwarded", isCorrect ? question.getMarks() : 0);
        return response;
    }

    /**
     * Adds a new question entity with input validations.
     */
    public QuestionEntity addQuestion(QuestionEntity question) {
        validateQuestion(question);
        return questionRepository.save(question);
    }

    /**
     * Updates an existing question entity.
     */
    public QuestionEntity updateQuestion(Long id, QuestionEntity updated) {
        QuestionEntity existing = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + id));

        validateQuestion(updated);

        existing.setCategory(updated.getCategory().toUpperCase());
        existing.setSubCategory(updated.getSubCategory());
        existing.setQuestionText(updated.getQuestionText());
        existing.setOptionA(updated.getOptionA());
        existing.setOptionB(updated.getOptionB());
        existing.setOptionC(updated.getOptionC());
        existing.setOptionD(updated.getOptionD());
        existing.setCorrectOption(updated.getCorrectOption().toUpperCase());
        existing.setExplanation(updated.getExplanation());
        existing.setDifficulty(updated.getDifficulty());
        existing.setMarks(updated.getMarks());

        return questionRepository.save(existing);
    }

    /**
     * Deletes a question by ID.
     */
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with ID: " + id);
        }
        questionRepository.deleteById(id);
    }

    private void validateQuestion(QuestionEntity q) {
        if (q.getQuestionText() == null || q.getQuestionText().trim().isEmpty()) {
            throw new ValidationException("Question text is required");
        }
        if (q.getOptionA() == null || q.getOptionB() == null ||
            q.getOptionC() == null || q.getOptionD() == null) {
            throw new ValidationException("All four options (A, B, C, D) are required");
        }
        if (q.getCorrectOption() == null || !q.getCorrectOption().matches("(?i)[ABCD]")) {
            throw new ValidationException("Correct option must be A, B, C, or D");
        }
        if (q.getCategory() == null || q.getCategory().trim().isEmpty()) {
            throw new ValidationException("Category is required (APTITUDE, LOGICAL, or TECHNICAL)");
        }
    }
}
