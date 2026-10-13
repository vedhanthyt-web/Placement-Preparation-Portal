package com.college.placementportal.service;

import com.college.placementportal.exception.ValidationException;
import com.college.placementportal.model.Admin;
import com.college.placementportal.model.Student;
import com.college.placementportal.model.User;
import com.college.placementportal.repository.AdminRepository;
import com.college.placementportal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public AuthService(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    /**
     * Registers a new student candidate with strict validations.
     */
    public Student registerStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new ValidationException("An account with email " + student.getEmail() + " already exists.");
        }
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            throw new ValidationException("An account with roll number " + student.getRollNumber() + " already exists.");
        }
        if (student.getCgpa() < 0.0 || student.getCgpa() > 10.0) {
            throw new ValidationException("CGPA must be between 0.0 and 10.0");
        }
        if (student.getBacklogs() < 0) {
            throw new ValidationException("Backlogs cannot be negative");
        }

        student.setRole("STUDENT");
        return studentRepository.save(student);
    }

    /**
     * Authenticates user (Student or Admin) based on email and password.
     */
    public User login(String email, String password) {
        if (email == null || password == null) {
            throw new ValidationException("Email and password are required");
        }
        String cleanEmail = email.trim().toLowerCase();

        // 1. Check Admin repository first
        Optional<Admin> adminOpt = adminRepository.findByEmail(cleanEmail);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getPassword().equals(password)) {
                return admin;
            }
            throw new ValidationException("Invalid email or password");
        }

        // 2. Check Student repository
        Optional<Student> studentOpt = studentRepository.findByEmail(cleanEmail);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (student.getPassword().equals(password)) {
                return student;
            }
            throw new ValidationException("Invalid email or password");
        }

        throw new ValidationException("No account registered with email " + email);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Student not found with ID: " + id));
    }
}
