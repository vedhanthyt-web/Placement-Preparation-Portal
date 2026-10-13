package com.college.placementportal.model;

import jakarta.persistence.*;

/**
 * OOP Concept: Encapsulation, Interface Implementation, Method Overriding, Method Overloading.
 * PlacementDrive models company recruitment drives and evaluates candidate eligibility.
 */
@Entity
@Table(name = "placement_drives")
public class PlacementDrive implements Trackable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobRole;

    @Column(nullable = false)
    private double packageLpa;

    @Column(nullable = false)
    private double minCgpa;

    @Column(nullable = false)
    private int maxBacklogs;

    @Column(nullable = false)
    private double minTestScore; // Minimum portal mock test average required

    @Column(nullable = false)
    private String driveDate;

    @Column(nullable = false)
    private String status = "UPCOMING"; // UPCOMING, ACTIVE, COMPLETED

    @Column(length = 1000)
    private String jobDescription;

    public PlacementDrive() {
    }

    public PlacementDrive(String companyName, String jobRole, double packageLpa,
                          double minCgpa, int maxBacklogs, double minTestScore,
                          String driveDate, String status, String jobDescription) {
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.packageLpa = packageLpa;
        this.minCgpa = minCgpa;
        this.maxBacklogs = maxBacklogs;
        this.minTestScore = minTestScore;
        this.driveDate = driveDate;
        this.status = status != null ? status : "UPCOMING";
        this.jobDescription = jobDescription;
    }

    // Method Overriding: Implements Trackable interface
    @Override
    public boolean isEligible(Student student) {
        if (student == null) {
            return false;
        }
        return isEligible(student.getCgpa(), student.getBacklogs(), student.getAverageScore());
    }

    // Method Overloading (Compile-Time Polymorphism): Overloaded method variant 1
    public boolean isEligible(double studentCgpa, int studentBacklogs) {
        return studentCgpa >= this.minCgpa && studentBacklogs <= this.maxBacklogs;
    }

    // Method Overloading (Compile-Time Polymorphism): Overloaded method variant 2
    public boolean isEligible(double studentCgpa, int studentBacklogs, double averageTestScore) {
        return studentCgpa >= this.minCgpa &&
               studentBacklogs <= this.maxBacklogs &&
               averageTestScore >= this.minTestScore;
    }

    // Encapsulation: Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public double getPackageLpa() {
        return packageLpa;
    }

    public void setPackageLpa(double packageLpa) {
        this.packageLpa = packageLpa;
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public int getMaxBacklogs() {
        return maxBacklogs;
    }

    public void setMaxBacklogs(int maxBacklogs) {
        this.maxBacklogs = maxBacklogs;
    }

    public double getMinTestScore() {
        return minTestScore;
    }

    public void setMinTestScore(double minTestScore) {
        this.minTestScore = minTestScore;
    }

    public String getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(String driveDate) {
        this.driveDate = driveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}
