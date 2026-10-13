package com.college.placementportal.model;

/**
 * OOP Concept: Inheritance, Polymorphism, Method Overriding.
 * Concrete Question subclass for Quantitative Aptitude assessments.
 */
public class AptitudeQuestion extends Question {

    private String topic; // e.g., "Percentages", "Time and Work", "Permutation & Combination"

    public AptitudeQuestion() {
        super();
    }

    public AptitudeQuestion(Long id, String questionText, String optionA, String optionB,
                            String optionC, String optionD, String correctOption,
                            String explanation, String difficulty, int marks, String topic) {
        super(id, questionText, optionA, optionB, optionC, optionD, correctOption, explanation, difficulty, marks);
        this.topic = topic;
    }

    // Method Overriding: Polymorphic category definition
    @Override
    public String getCategory() {
        return "APTITUDE";
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "[Aptitude - " + topic + "] " + super.toString();
    }
}
