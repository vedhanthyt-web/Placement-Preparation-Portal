package com.college.placementportal.service;

import com.college.placementportal.model.Result;
import com.college.placementportal.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public List<Result> getStudentResults(Long studentId) {
        return resultRepository.findByStudentIdOrderByAttemptDateDesc(studentId);
    }

    public Map<String, Object> getStudentPerformanceSummary(Long studentId) {
        List<Result> results = resultRepository.findByStudentIdOrderByAttemptDateDesc(studentId);

        int totalTests = results.size();
        double highestPercentage = 0.0;
        double sumPercentage = 0.0;
        int totalQuestionsAttempted = 0;
        int totalCorrectAnswers = 0;

        for (Result r : results) {
            if (r.getPercentage() > highestPercentage) {
                highestPercentage = r.getPercentage();
            }
            sumPercentage += r.getPercentage();
            totalQuestionsAttempted += r.getTotalQuestions();
            totalCorrectAnswers += r.getCorrectAnswers();
        }

        double averagePercentage = totalTests > 0 ? (Math.round((sumPercentage / totalTests) * 10.0) / 10.0) : 0.0;
        double overallAccuracy = totalQuestionsAttempted > 0
                ? (Math.round(((double) totalCorrectAnswers / totalQuestionsAttempted) * 1000.0) / 10.0)
                : 0.0;

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalTests", totalTests);
        summary.put("highestPercentage", highestPercentage);
        summary.put("averagePercentage", averagePercentage);
        summary.put("overallAccuracy", overallAccuracy);
        summary.put("recentResults", results.size() > 5 ? results.subList(0, 5) : results);

        return summary;
    }
}
