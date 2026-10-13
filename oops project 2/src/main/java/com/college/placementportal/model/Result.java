package com.college.placementportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * OOP Concept: Encapsulation, Interface Implementation, Method Overriding.
 * Result represents the recorded outcome of an assessment and implements Scorable.
 */
@Entity
@Table(name = "results")
public class Result implements Scorable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String testTitle;

    @Column(nullable = false)
    private String category; // APTITUDE, LOGICAL, TECHNICAL, FULL_MOCK

    @Column(nullable = false)
    private int totalQuestions;

    @Column(nullable = false)
    private int correctAnswers;

    @Column(nullable = false)
    private int wrongAnswers;

    @Column(nullable = false)
    private int unattempted;

    @Column(nullable = false)
    private int scoreObtained;

    @Column(nullable = false)
    private int totalMarks;

    @Column(nullable = false)
    private double percentage;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private LocalDateTime attemptDate;

    public Result() {
        this.attemptDate = LocalDateTime.now();
    }

    public Result(Long studentId, String studentName, String testTitle, String category,
                  int totalQuestions, int correctAnswers, int wrongAnswers, int unattempted,
                  int scoreObtained, int totalMarks) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.testTitle = testTitle;
        this.category = category;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.unattempted = unattempted;
        this.scoreObtained = scoreObtained;
        this.totalMarks = totalMarks > 0 ? totalMarks : totalQuestions;
        this.attemptDate = LocalDateTime.now();
        this.percentage = calculatePercentage();
        this.grade = getPerformanceGrade();
    }

    // Method Overriding: Implementation of Scorable interface methods

    @Override
    public double calculatePercentage() {
        if (totalMarks <= 0) {
            return 0.0;
        }
        double val = ((double) scoreObtained / totalMarks) * 100.0;
        return Math.round(val * 10.0) / 10.0; // Rounded to 1 decimal place
    }

    @Override
    public String getPerformanceGrade() {
        double pct = this.percentage;
        if (pct >= 85.0) return "Distinction (A+)";
        if (pct >= 75.0) return "Excellent (A)";
        if (pct >= 60.0) return "Good (B)";
        if (pct >= 50.0) return "Average (C)";
        return "Needs Improvement (F)";
    }

    // Encapsulation: Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public int getUnattempted() {
        return unattempted;
    }

    public void setUnattempted(int unattempted) {
        this.unattempted = unattempted;
    }

    public int getScoreObtained() {
        return scoreObtained;
    }

    public void setScoreObtained(int scoreObtained) {
        this.scoreObtained = scoreObtained;
        this.percentage = calculatePercentage();
        this.grade = getPerformanceGrade();
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
        this.percentage = calculatePercentage();
        this.grade = getPerformanceGrade();
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDateTime getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(LocalDateTime attemptDate) {
        this.attemptDate = attemptDate;
    }
}
