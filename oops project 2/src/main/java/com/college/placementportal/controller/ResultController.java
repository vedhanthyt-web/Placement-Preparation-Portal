package com.college.placementportal.controller;

import com.college.placementportal.model.Result;
import com.college.placementportal.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Result>> getStudentResults(@PathVariable Long studentId) {
        List<Result> results = resultService.getStudentResults(studentId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/student/{studentId}/summary")
    public ResponseEntity<Map<String, Object>> getStudentSummary(@PathVariable Long studentId) {
        Map<String, Object> summary = resultService.getStudentPerformanceSummary(studentId);
        return ResponseEntity.ok(summary);
    }
}
