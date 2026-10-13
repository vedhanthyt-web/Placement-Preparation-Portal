package com.college.placementportal.model;

/**
 * OOP Concept: Abstraction, Encapsulation, Interface Implementation, Method Overriding.
 * Abstract class representing a generic Multiple-Choice Question (MCQ).
 * It implements Evaluatable, fulfilling the evaluation contract.
 */
public abstract class Question implements Evaluatable {

    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption; // "A", "B", "C", or "D"
    private String explanation;
    private String difficulty; // "EASY", "MEDIUM", "HARD"
    private int marks = 1;

    // Default Constructor
    public Question() {
    }

    // Parameterized Constructor
    public Question(Long id, String questionText, String optionA, String optionB,
                    String optionC, String optionD, String correctOption,
                    String explanation, String difficulty, int marks) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        setCorrectOption(correctOption);
        this.explanation = explanation;
        this.difficulty = difficulty != null ? difficulty : "MEDIUM";
        this.marks = marks > 0 ? marks : 1;
    }

    // Abstract method: Forces each specialized question type to state its domain category
    public abstract String getCategory();

    // Implementation of Evaluatable interface method (Demonstrating Polymorphic Evaluation)
    @Override
    public boolean evaluateAnswer(String selectedOption) {
        if (selectedOption == null || this.correctOption == null) {
            return false;
        }
        return this.correctOption.trim().equalsIgnoreCase(selectedOption.trim());
    }

    @Override
    public int getMarks() {
        return marks;
    }

    // Encapsulation: Getters and Setters with validations

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (correctOption == null || !correctOption.matches("(?i)[ABCD]")) {
            throw new IllegalArgumentException("Correct option must be one of A, B, C, or D");
        }
        this.correctOption = correctOption.toUpperCase();
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

    public void setMarks(int marks) {
        this.marks = marks > 0 ? marks : 1;
    }

    @Override
    public String toString() {
        return "[" + getCategory() + "] Q" + id + ": " + questionText + " (Answer: " + correctOption + ")";
    }
}
