package com.college.placementportal.model;

import jakarta.persistence.*;

/**
 * JPA Entity for Question persistence in MySQL / Database.
 * Connects Relational Database storage to Polymorphic OOP Domain Hierarchy.
 */
@Entity
@Table(name = "questions")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category; // "APTITUDE", "LOGICAL", "TECHNICAL"

    @Column(nullable = false)
    private String subCategory; // e.g., Topic / Reasoning Type / Language

    @Column(nullable = false, length = 1000)
    private String questionText;

    @Column(nullable = false, length = 500)
    private String optionA;

    @Column(nullable = false, length = 500)
    private String optionB;

    @Column(nullable = false, length = 500)
    private String optionC;

    @Column(nullable = false, length = 500)
    private String optionD;

    @Column(nullable = false, length = 1)
    private String correctOption; // A, B, C, D

    @Column(length = 1000)
    private String explanation;

    @Column(nullable = false)
    private String difficulty = "MEDIUM"; // EASY, MEDIUM, HARD

    @Column(nullable = false)
    private int marks = 1;

    public QuestionEntity() {
    }

    public QuestionEntity(String category, String subCategory, String questionText,
                          String optionA, String optionB, String optionC, String optionD,
                          String correctOption, String explanation, String difficulty, int marks) {
        this.category = category;
        this.subCategory = subCategory;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.explanation = explanation;
        this.difficulty = difficulty;
        this.marks = marks;
    }

    /**
     * Factory Method Pattern / Polymorphism:
     * Transforms relational database entity into the specialized polymorphic OOP Question subclass.
     */
    public Question toDomainQuestion() {
        if ("APTITUDE".equalsIgnoreCase(category)) {
            return new AptitudeQuestion(id, questionText, optionA, optionB, optionC, optionD,
                    correctOption, explanation, difficulty, marks, subCategory);
        } else if ("LOGICAL".equalsIgnoreCase(category)) {
            return new LogicalQuestion(id, questionText, optionA, optionB, optionC, optionD,
                    correctOption, explanation, difficulty, marks, subCategory);
        } else {
            return new TechnicalQuestion(id, questionText, optionA, optionB, optionC, optionD,
                    correctOption, explanation, difficulty, marks, subCategory);
        }
    }

    // Encapsulation: Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
