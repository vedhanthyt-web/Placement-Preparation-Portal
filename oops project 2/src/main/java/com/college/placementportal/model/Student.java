package com.college.placementportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * OOP Concept: Inheritance, Encapsulation, Constructor Chaining, Method Overriding.
 * Student inherits state and behavior from User ('is-a' relationship).
 */
@Entity
@Table(name = "students")
public class Student extends User {

    @Column(nullable = false, unique = true)
    private String rollNumber;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private double cgpa;

    @Column(nullable = false)
    private int backlogs;

    @Column(nullable = false)
    private int testsAttempted = 0;

    @Column(nullable = false)
    private double averageScore = 0.0;

    // Default Constructor
    public Student() {
        super();
        setRole("STUDENT");
    }

    // Parameterized Constructor demonstrating Constructor Chaining via super(...)
    public Student(String name, String email, String password, String rollNumber,
                   String department, double cgpa, int backlogs) {
        super(name, email, password, "STUDENT"); // Invokes parent constructor
        this.rollNumber = rollNumber;
        this.department = department;
        setCgpa(cgpa); // Encapsulated validation
        setBacklogs(backlogs);
    }

    // Method Overriding: Concrete implementation of abstract method from User
    @Override
    public String getRoleDescription() {
        return "Student candidate preparing for campus recruitment drives in " + department;
    }

    // Business method demonstrating Encapsulation of internal score tracking
    public void updatePerformance(double newScore) {
        double currentTotal = this.averageScore * this.testsAttempted;
        this.testsAttempted++;
        this.averageScore = Math.round(((currentTotal + newScore) / this.testsAttempted) * 10.0) / 10.0;
    }

    // Encapsulated Getters & Setters with Business Validations

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        if (cgpa < 0.0 || cgpa > 10.0) {
            throw new IllegalArgumentException("CGPA must be between 0.0 and 10.0");
        }
        this.cgpa = cgpa;
    }

    public int getBacklogs() {
        return backlogs;
    }

    public void setBacklogs(int backlogs) {
        if (backlogs < 0) {
            throw new IllegalArgumentException("Backlogs count cannot be negative");
        }
        this.backlogs = backlogs;
    }

    public int getTestsAttempted() {
        return testsAttempted;
    }

    public void setTestsAttempted(int testsAttempted) {
        this.testsAttempted = testsAttempted;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    // Method Overriding of toString()
    @Override
    public String toString() {
        return "Student[rollNumber=" + rollNumber + ", name=" + getName() +
               ", dept=" + department + ", cgpa=" + cgpa + ", avgScore=" + averageScore + "]";
    }
}
