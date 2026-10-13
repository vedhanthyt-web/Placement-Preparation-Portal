package com.college.placementportal.controller;

import com.college.placementportal.model.Question;
import com.college.placementportal.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Retrieves questions by category (Aptitude, Logical, Technical).
     * Returns polymorphic Question subclasses.
     */
    @GetMapping("/practice")
    public ResponseEntity<List<Question>> getPracticeQuestions(
            @RequestParam(defaultValue = "ALL") String category) {
        List<Question> questions = questionService.getPracticeQuestions(category);
        return ResponseEntity.ok(questions);
    }

    /**
     * Validates an answer for practice mode and provides immediate explanation.
     */
    @PostMapping("/practice/evaluate")
    public ResponseEntity<Map<String, Object>> evaluatePracticeAnswer(
            @RequestBody Map<String, Object> payload) {
        Long questionId = Long.valueOf(payload.get("questionId").toString());
        String selectedOption = (String) payload.get("selectedOption");
        Map<String, Object> result = questionService.evaluatePracticeAnswer(questionId, selectedOption);
        return ResponseEntity.ok(result);
    }
}
