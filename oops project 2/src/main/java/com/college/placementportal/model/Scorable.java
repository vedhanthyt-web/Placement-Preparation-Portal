package com.college.placementportal.model;

/**
 * OOP Interface: Demonstrates Abstraction for score and grading computation.
 */
public interface Scorable {
    /**
     * Calculates the percentage attained.
     * @return percentage as a double
     */
    double calculatePercentage();

    /**
     * Derives letter grade based on percentage.
     * @return Grade string (e.g., A+, A, B, C, F)
     */
    String getPerformanceGrade();
}
