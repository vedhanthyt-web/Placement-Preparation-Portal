package com.college.placementportal.model;

/**
 * OOP Concept: Inheritance, Polymorphism, Method Overriding.
 * Concrete Question subclass for Logical Reasoning assessments.
 */
public class LogicalQuestion extends Question {

    private String reasoningType; // e.g., "Blood Relations", "Syllogism", "Number Series"

    public LogicalQuestion() {
        super();
    }

    public LogicalQuestion(Long id, String questionText, String optionA, String optionB,
                           String optionC, String optionD, String correctOption,
                           String explanation, String difficulty, int marks, String reasoningType) {
        super(id, questionText, optionA, optionB, optionC, optionD, correctOption, explanation, difficulty, marks);
        this.reasoningType = reasoningType;
    }

    // Method Overriding: Polymorphic category definition
    @Override
    public String getCategory() {
        return "LOGICAL";
    }

    public String getReasoningType() {
        return reasoningType;
    }

    public void setReasoningType(String reasoningType) {
        this.reasoningType = reasoningType;
    }

    @Override
    public String toString() {
        return "[Logical - " + reasoningType + "] " + super.toString();
    }
}
