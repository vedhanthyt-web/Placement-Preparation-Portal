package com.college.placementportal.model;

/**
 * OOP Interface: Demonstrates Abstraction and Interface Contracts.
 * Any component that can be evaluated (e.g., questions, assignments)
 * must implement this contract.
 */
public interface Evaluatable {
    /**
     * Evaluates whether the user's selected option is correct.
     * @param selectedOption The option selected (A, B, C, or D)
     * @return true if correct, false otherwise
     */
    boolean evaluateAnswer(String selectedOption);

    /**
     * Returns the marks awarded for this item.
     * @return Marks awarded
     */
    int getMarks();
}
