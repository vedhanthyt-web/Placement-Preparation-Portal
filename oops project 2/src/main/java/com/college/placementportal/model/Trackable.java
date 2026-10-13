package com.college.placementportal.model;

/**
 * OOP Interface: Contract for placement and eligibility tracking.
 */
public interface Trackable {
    /**
     * Checks if a student is eligible for a placement opportunity.
     * @param student The candidate student
     * @return true if candidate satisfies all criteria
     */
    boolean isEligible(Student student);
}
