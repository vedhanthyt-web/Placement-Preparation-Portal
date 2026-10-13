-- ===================================================================
-- Placement Preparation Portal - MySQL Database Schema
-- Database Name: placement_portal_db
-- ===================================================================

CREATE DATABASE IF NOT EXISTS placement_portal_db;
USE placement_portal_db;

-- 1. Students Table (Inherits from User)
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    cgpa DOUBLE NOT NULL,
    backlogs INT NOT NULL DEFAULT 0,
    tests_attempted INT NOT NULL DEFAULT 0,
    average_score DOUBLE NOT NULL DEFAULT 0.0
);

-- 2. Admins Table (Inherits from User)
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    department_managed VARCHAR(100) NOT NULL
);

-- 3. Questions Table
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50) NOT NULL, -- APTITUDE, LOGICAL, TECHNICAL
    sub_category VARCHAR(100) NOT NULL, -- Topic, Reasoning Type, Language
    question_text TEXT NOT NULL,
    option_a VARCHAR(500) NOT NULL,
    option_b VARCHAR(500) NOT NULL,
    option_c VARCHAR(500) NOT NULL,
    option_d VARCHAR(500) NOT NULL,
    correct_option CHAR(1) NOT NULL, -- A, B, C, D
    explanation TEXT,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    marks INT NOT NULL DEFAULT 1
);

-- 4. Results Table
CREATE TABLE IF NOT EXISTS results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    test_title VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    wrong_answers INT NOT NULL,
    unattempted INT NOT NULL,
    score_obtained INT NOT NULL,
    total_marks INT NOT NULL,
    percentage DOUBLE NOT NULL,
    grade VARCHAR(50) NOT NULL,
    attempt_date DATETIME NOT NULL
);

-- 5. Placement Drives Table
CREATE TABLE IF NOT EXISTS placement_drives (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    job_role VARCHAR(100) NOT NULL,
    package_lpa DOUBLE NOT NULL,
    min_cgpa DOUBLE NOT NULL,
    max_backlogs INT NOT NULL,
    min_test_score DOUBLE NOT NULL,
    drive_date VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UPCOMING',
    job_description TEXT
);
