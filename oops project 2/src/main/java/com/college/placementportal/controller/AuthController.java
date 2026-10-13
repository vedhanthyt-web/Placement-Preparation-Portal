package com.college.placementportal.controller;

import com.college.placementportal.model.Student;
import com.college.placementportal.model.User;
import com.college.placementportal.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        Student created = authService.registerStudent(student);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        User user = authService.login(email, password);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudentProfile(@PathVariable Long id) {
        Student student = authService.getStudentById(id);
        return ResponseEntity.ok(student);
    }
}
