package com.college.placementportal.controller;

import com.college.placementportal.model.PlacementDrive;
import com.college.placementportal.service.EligibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drives")
public class PlacementDriveController {

    private final EligibilityService eligibilityService;

    public PlacementDriveController(EligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    @GetMapping
    public ResponseEntity<List<PlacementDrive>> getAllDrives() {
        List<PlacementDrive> drives = eligibilityService.getAllDrives();
        return ResponseEntity.ok(drives);
    }

    @GetMapping("/eligibility/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getStudentEligibility(@PathVariable Long studentId) {
        List<Map<String, Object>> report = eligibilityService.evaluateStudentEligibility(studentId);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/check-hypothetical")
    public ResponseEntity<List<Map<String, Object>>> checkHypotheticalEligibility(@RequestBody Map<String, Object> payload) {
        double cgpa = Double.parseDouble(payload.get("cgpa").toString());
        int backlogs = Integer.parseInt(payload.get("backlogs").toString());
        List<Map<String, Object>> result = eligibilityService.evaluateHypotheticalEligibility(cgpa, backlogs);
        return ResponseEntity.ok(result);
    }
}
