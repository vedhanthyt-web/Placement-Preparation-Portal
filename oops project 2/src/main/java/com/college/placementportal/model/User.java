package com.college.placementportal.model;

import jakarta.persistence.*;

/**
 * OOP Concept: Abstraction & Encapsulation.
 * Abstract base class for all system users (Students, Admins).
 * - Declares common attributes with private access modifiers (Encapsulation).
 * - Enforces concrete subclasses to define their specific role description (Abstraction).
 */
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // "STUDENT" or "ADMIN"

    // Default Constructor (Required by JPA and default instantiation)
    public User() {
    }

    // Parameterized Constructor (Constructor Chaining / OOP Constructor Concept)
    public User(String name, String email, String password, String role) {
        setName(name);
        setEmail(email);
        setPassword(password);
        this.role = role;
    }

    // Abstract method: Forces subclasses to implement their specific role behavior
    public abstract String getRoleDescription();

    // Getters and Setters with Encapsulation & Validation rules

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Valid email address is required");
        }
        this.email = email.trim().toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must have at least 4 characters");
        }
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User[id=" + id + ", name=" + name + ", email=" + email + ", role=" + role + "]";
    }
}
