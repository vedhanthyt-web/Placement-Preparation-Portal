package com.college.placementportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * OOP Concept: Inheritance, Encapsulation, Constructor Chaining, Method Overriding.
 * Admin represents placement coordinators and administrators managing tests & drives.
 */
@Entity
@Table(name = "admins")
public class Admin extends User {

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    private String departmentManaged;

    // Default Constructor
    public Admin() {
        super();
        setRole("ADMIN");
    }

    // Parameterized Constructor
    public Admin(String name, String email, String password, String employeeId, String departmentManaged) {
        super(name, email, password, "ADMIN");
        this.employeeId = employeeId;
        this.departmentManaged = departmentManaged;
    }

    // Overriding abstract method from User
    @Override
    public String getRoleDescription() {
        return "Placement Administrator managing question bank and recruitment drives for " + departmentManaged;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartmentManaged() {
        return departmentManaged;
    }

    public void setDepartmentManaged(String departmentManaged) {
        this.departmentManaged = departmentManaged;
    }

    @Override
    public String toString() {
        return "Admin[empId=" + employeeId + ", name=" + getName() + ", dept=" + departmentManaged + "]";
    }
}
