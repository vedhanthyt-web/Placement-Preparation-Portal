package com.college.placementportal.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * OOP Concept: Encapsulation, Composition & Aggregation.
 * MockTest encapsulates a live timed test session composed of multiple polymorphic Questions.
 */
public class MockTest {

    private String testId;
    private Long studentId;
    private String title;
    private String category; // "APTITUDE", "LOGICAL", "TECHNICAL", or "FULL_MOCK"
    private int durationMinutes;
    private LocalDateTime startTime;
    private boolean submitted;
    private List<Question> questions = new ArrayList<>();

    public MockTest() {
        this.startTime = LocalDateTime.now();
        this.submitted = false;
    }

    public MockTest(String testId, Long studentId, String title, String category,
                    int durationMinutes, List<Question> questions) {
        this.testId = testId;
        this.studentId = studentId;
        this.title = title;
        this.category = category;
        this.durationMinutes = durationMinutes;
        this.questions = questions != null ? questions : new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.submitted = false;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public int getMaxScore() {
        int sum = 0;
        for (Question q : questions) {
            sum += q.getMarks();
        }
        return sum;
    }

    // Encapsulation: Getters and Setters

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
