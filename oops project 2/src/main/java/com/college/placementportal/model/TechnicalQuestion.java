package com.college.placementportal.model;

/**
 * OOP Concept: Inheritance, Polymorphism, Method Overriding.
 * Concrete Question subclass for Technical & Core Java MCQ assessments.
 */
public class TechnicalQuestion extends Question {

    private String programmingLanguage; // e.g., "Java", "SQL", "OOP", "Data Structures"

    public TechnicalQuestion() {
        super();
    }

    public TechnicalQuestion(Long id, String questionText, String optionA, String optionB,
                             String optionC, String optionD, String correctOption,
                             String explanation, String difficulty, int marks, String programmingLanguage) {
        super(id, questionText, optionA, optionB, optionC, optionD, correctOption, explanation, difficulty, marks);
        this.programmingLanguage = programmingLanguage;
    }

    // Method Overriding: Polymorphic category definition
    @Override
    public String getCategory() {
        return "TECHNICAL";
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public String toString() {
        return "[Technical - " + programmingLanguage + "] " + super.toString();
    }
}
