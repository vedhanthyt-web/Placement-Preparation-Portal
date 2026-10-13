package com.college.placementportal.controller;

import com.college.placementportal.model.PlacementDrive;
import com.college.placementportal.model.QuestionEntity;
import com.college.placementportal.repository.PlacementDriveRepository;
import com.college.placementportal.repository.QuestionRepository;
import com.college.placementportal.repository.ResultRepository;
import com.college.placementportal.repository.StudentRepository;
import com.college.placementportal.service.EligibilityService;
import com.college.placementportal.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final QuestionService questionService;
    private final EligibilityService eligibilityService;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final PlacementDriveRepository driveRepository;

    public AdminController(QuestionService questionService,
                           EligibilityService eligibilityService,
                           StudentRepository studentRepository,
                           QuestionRepository questionRepository,
                           ResultRepository resultRepository,
                           PlacementDriveRepository driveRepository) {
        this.questionService = questionService;
        this.eligibilityService = eligibilityService;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.driveRepository = driveRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalQuestions", questionRepository.count());
        stats.put("totalTestsTaken", resultRepository.count());
        stats.put("totalDrives", driveRepository.count());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionEntity>> getAllQuestions() {
        List<QuestionEntity> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/questions")
    public ResponseEntity<QuestionEntity> addQuestion(@RequestBody QuestionEntity question) {
        QuestionEntity created = questionService.addQuestion(question);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<QuestionEntity> updateQuestion(@PathVariable Long id, @RequestBody QuestionEntity question) {
        QuestionEntity updated = questionService.updateQuestion(id, question);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Question deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/drives")
    public ResponseEntity<PlacementDrive> addDrive(@RequestBody PlacementDrive drive) {
        PlacementDrive created = eligibilityService.addDrive(drive);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/drives/{id}")
    public ResponseEntity<Map<String, String>> deleteDrive(@PathVariable Long id) {
        eligibilityService.deleteDrive(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Placement drive deleted successfully");
        return ResponseEntity.ok(response);
    }
}
