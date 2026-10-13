package com.college.placementportal.controller;

import com.college.placementportal.model.MockTest;
import com.college.placementportal.service.MockTestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mocktest")
public class MockTestController {

    private final MockTestService mockTestService;

    public MockTestController(MockTestService mockTestService) {
        this.mockTestService = mockTestService;
    }

    @PostMapping("/start")
    public ResponseEntity<MockTest> startMockTest(@RequestBody Map<String, Object> payload) {
        Long studentId = Long.valueOf(payload.get("studentId").toString());
        String category = (String) payload.getOrDefault("category", "FULL_MOCK");
        int count = payload.containsKey("count") ? Integer.parseInt(payload.get("count").toString()) : 5;
        int durationMinutes = payload.containsKey("durationMinutes") ? Integer.parseInt(payload.get("durationMinutes").toString()) : 10;

        MockTest test = mockTestService.startMockTest(studentId, category, count, durationMinutes);
        return ResponseEntity.ok(test);
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitMockTest(@RequestBody Map<String, Object> payload) {
        String testId = (String) payload.get("testId");
        Object answersObj = payload.get("answers");

        Map<Long, String> parsedAnswers = new HashMap<>();
        if (answersObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> rawMap = (Map<Object, Object>) answersObj;
            for (Map.Entry<Object, Object> entry : rawMap.entrySet()) {
                Long qId = Long.valueOf(entry.getKey().toString());
                String opt = entry.getValue() != null ? entry.getValue().toString() : null;
                parsedAnswers.put(qId, opt);
            }
        }

        Map<String, Object> result = mockTestService.submitMockTest(testId, parsedAnswers);
        return ResponseEntity.ok(result);
    }
}
