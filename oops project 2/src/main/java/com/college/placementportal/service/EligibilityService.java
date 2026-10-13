package com.college.placementportal.service;

import com.college.placementportal.exception.ResourceNotFoundException;
import com.college.placementportal.model.PlacementDrive;
import com.college.placementportal.model.Student;
import com.college.placementportal.repository.PlacementDriveRepository;
import com.college.placementportal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EligibilityService {

    private final PlacementDriveRepository driveRepository;
    private final StudentRepository studentRepository;

    public EligibilityService(PlacementDriveRepository driveRepository,
                              StudentRepository studentRepository) {
        this.driveRepository = driveRepository;
        this.studentRepository = studentRepository;
    }

    public List<PlacementDrive> getAllDrives() {
        return driveRepository.findAll();
    }

    public PlacementDrive addDrive(PlacementDrive drive) {
        return driveRepository.save(drive);
    }

    public void deleteDrive(Long id) {
        if (!driveRepository.existsById(id)) {
            throw new ResourceNotFoundException("Placement drive not found with ID: " + id);
        }
        driveRepository.deleteById(id);
    }

    /**
     * OOP Concept: Polymorphism & Interface Execution.
     * Evaluates a student against all placement drives using Trackable.isEligible(student).
     */
    public List<Map<String, Object>> evaluateStudentEligibility(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<PlacementDrive> allDrives = driveRepository.findAll();
        List<Map<String, Object>> evaluationReport = new ArrayList<>();

        for (PlacementDrive drive : allDrives) {
            // Polymorphic call through Trackable contract
            boolean eligible = drive.isEligible(student);

            // Detailed reason checking for student UI
            boolean meetsCgpa = student.getCgpa() >= drive.getMinCgpa();
            boolean meetsBacklogs = student.getBacklogs() <= drive.getMaxBacklogs();
            boolean meetsScore = student.getAverageScore() >= drive.getMinTestScore();

            Map<String, Object> item = new HashMap<>();
            item.put("driveId", drive.getId());
            item.put("companyName", drive.getCompanyName());
            item.put("jobRole", drive.getJobRole());
            item.put("packageLpa", drive.getPackageLpa());
            item.put("minCgpa", drive.getMinCgpa());
            item.put("maxBacklogs", drive.getMaxBacklogs());
            item.put("minTestScore", drive.getMinTestScore());
            item.put("driveDate", drive.getDriveDate());
            item.put("status", drive.getStatus());
            item.put("jobDescription", drive.getJobDescription());
            item.put("isEligible", eligible);
            item.put("meetsCgpa", meetsCgpa);
            item.put("meetsBacklogs", meetsBacklogs);
            item.put("meetsScore", meetsScore);

            evaluationReport.add(item);
        }

        return evaluationReport;
    }

    /**
     * Method Overloading: Evaluate hypothetical CGPA and backlogs without student record
     */
    public List<Map<String, Object>> evaluateHypotheticalEligibility(double cgpa, int backlogs) {
        List<PlacementDrive> allDrives = driveRepository.findAll();
        List<Map<String, Object>> list = new ArrayList<>();

        for (PlacementDrive drive : allDrives) {
            boolean eligible = drive.isEligible(cgpa, backlogs);
            Map<String, Object> map = new HashMap<>();
            map.put("companyName", drive.getCompanyName());
            map.put("jobRole", drive.getJobRole());
            map.put("packageLpa", drive.getPackageLpa());
            map.put("isEligible", eligible);
            list.add(map);
        }
        return list;
    }
}
