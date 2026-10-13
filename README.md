# Placement Preparation Portal
live demo below 
placement-preparation-portal-production.up.railway.app
**College Java Object-Oriented Programming (OOP) Capstone Project**

A complete, production-ready web application built with **Java 17**, **Spring Boot 3**, **MySQL** (with H2 in-memory test fallback), and a modern, responsive **HTML5/CSS3/JavaScript** frontend.

Designed specifically to prepare college students for campus placements (Aptitude, Logical Reasoning, Technical Java & OOP MCQs, Timed Mock Assessments, Results Analytics, and Company Eligibility Tracking), while demonstrating core **Object-Oriented Programming (OOP) principles in pure Java** that are crystal clear to explain during viva examinations.

---

## 🌟 Key Features

1. **Student Registration & Login**
   - Candidate registration with validation (CGPA bounds `0.0 - 10.0`, non-negative backlogs, unique email & roll number).
   - Role-based authentication (`STUDENT` vs `ADMIN`).
   - 1-click viva demo accounts for instant evaluation.

2. **Placement Preparation Dashboard**
   - Real-time **Placement Readiness Score** computed from academic CGPA, backlogs penalty, and assessment test performance.
   - Live metrics: Tests Attempted, Average Score %, Highest Score %, and Eligible Companies count.
   - Quick navigation into Aptitude, Logical, and Technical tracks.

3. **MCQ Practice Zone**
   - Practice categorized by **Quantitative Aptitude**, **Logical Reasoning**, and **Technical Java & OOP**.
   - Immediate option checking with explanations and solution steps.

4. **Timed Mock Test Engine**
   - Randomized mock assessments generated across tracks (Full Mock or Category-specific).
   - Live browser countdown timer with automatic test submission on timer expiry.
   - Detailed result breakdown with percentage, letter grade, correct/wrong counts, and per-question explanations.

5. **Results History & Performance Tracking**
   - Attempt-by-attempt log tracking test title, track, timestamp, marks, percentage, and grade badge.
   - Aggregate analytics: cumulative average score, peak performance, and overall accuracy.

6. **Placement Eligibility Tracker**
   - Evaluates student eligibility against hiring criteria of top recruiters (Google, Microsoft, Amazon, TCS Digital, Cognizant).
   - Color-coded criteria checklist: CGPA threshold met/unmet, Backlogs criteria satisfied/violated, Mock test cutoff cleared/pending.
   - **Interactive 'What-If' Simulator**: Tests hypothetical scenarios (e.g., "If my CGPA is 8.0 with 0 backlogs, which companies can I apply for?").

7. **Admin Placement Officer Console**
   - System overview metrics (total questions, registered candidates, tests evaluated, active drives).
   - Complete Question Bank CRUD: add new questions, specify difficulty and explanations, delete questions.
   - Recruitment Drive Management: schedule new company drives, configure package (LPA), minimum CGPA, and test score cutoffs.

8. **Embedded OOP Viva Knowledge Hub**
   - In-app reference cards explaining how each OOP principle is modeled in the project code.

---

## 🎓 Java OOP Concepts Viva Guide

Each core concept of Object-Oriented Programming is intentionally demonstrated in explicit, readable Java classes:

| OOP Concept | Project Classes / Methods | Implementation Details & Viva Explanation |
| :--- | :--- | :--- |
| **Encapsulation** | `User`, `Student`, `Question`, `Result`, `PlacementDrive` | All fields are declared `private`. Access is guarded via getters, setters, and business validation methods (e.g., verifying `cgpa` is strictly between `0.0` and `10.0`, `backlogs >= 0`, `password` length &ge; 4). Score updating is encapsulated inside `student.updatePerformance(score)`.<br><br>*Viva Answer:* "Encapsulation binds data and code into a single unit, hiding the internal representation of an object to protect it from invalid state mutations." |
| **Inheritance** | `User` $\rightarrow$ `Student`<br>`User` $\rightarrow$ `Admin`<br>`Question` $\rightarrow$ `AptitudeQuestion`<br>`Question` $\rightarrow$ `LogicalQuestion`<br>`Question` $\rightarrow$ `TechnicalQuestion` | Modeled as clean 'is-a' hierarchies. `Student` and `Admin` extend `User`, inheriting `id`, `name`, `email`, `password`, and `role`. Specialized question classes extend `Question`, inheriting question text, choices, marks, and explanation while adding domain-specific fields like `topic`, `reasoningType`, or `programmingLanguage`.<br><br>*Viva Answer:* "Inheritance enables code reusability by allowing derived classes to inherit common fields and methods from a base class without duplication." |
| **Polymorphism (Runtime)** | `Question.evaluateAnswer(String selectedOption)`<br>`MockTestService`<br>`User.getRoleDescription()` | In `MockTestService`, test grading invokes `question.evaluateAnswer(chosen)` using base class reference `Question`. At runtime, Dynamic Method Dispatch executes the appropriate method for each question type.<br><br>*Viva Answer:* "Runtime polymorphism allows a single interface or base reference to invoke specialized subclass implementations determined dynamically at execution time." |
| **Polymorphism (Compile-Time)** | `PlacementDrive.isEligible(...)`<br>`EligibilityService` | Method Overloading: `PlacementDrive` defines `isEligible(Student student)`, `isEligible(double cgpa, int backlogs)`, and `isEligible(double cgpa, int backlogs, double avgScore)`. Same method name with different parameter signatures.<br><br>*Viva Answer:* "Method Overloading achieves compile-time polymorphism by allowing multiple methods in the same class to share a name with distinct parameter lists." |
| **Abstraction** | `abstract class User`<br>`abstract class Question` | Base classes cannot be instantiated directly. They define abstract methods `public abstract String getRoleDescription();` and `public abstract String getCategory();` which force concrete subclasses to define their specific identity.<br><br>*Viva Answer:* "Abstraction hides complex implementation details, exposing only essential interfaces and enforcing behavioral contracts on subclasses." |
| **Interfaces** | `Evaluatable`, `Scorable`, `Trackable` | `Evaluatable`: Defines question evaluation (`evaluateAnswer`, `getMarks`).<br>`Scorable`: Defines grading and percentage calculation (`calculatePercentage`, `getPerformanceGrade`).<br>`Trackable`: Defines placement drive eligibility contract (`isEligible`).<br><br>*Viva Answer:* "Interfaces specify what a class must do without defining how. They decouple contract from implementation and enable multiple inheritance of type in Java." |
| **Constructors & Chaining** | `Student`, `Admin`, `Question` subclasses | Subclasses chain to parent constructors using `super(...)` (e.g., `super(name, email, password, "STUDENT");`), ensuring parent invariants and fields are safely initialized before child extensions run.<br><br>*Viva Answer:* "Constructor chaining via `super(...)` guarantees that the base class state is properly initialized before subclass-specific initialization occurs." |
| **Method Overriding** | `@Override` in `Student`, `Admin`, `Result`, `Question` subclasses | Child classes override abstract methods (`getRoleDescription()`, `getCategory()`) and interface methods (`evaluateAnswer()`, `calculatePercentage()`, `getPerformanceGrade()`, `isEligible()`), as well as `toString()`.<br><br>*Viva Answer:* "Method overriding allows a subclass to provide a specific implementation of a method that is already provided by its superclass or interface." |

---

## 🏗️ Architecture & Package Structure

The project follows a standard **Layered Architecture (N-Tier)**:

```
src/main/java/com/college/placementportal/
├── PlacementPortalApplication.java       // Main Spring Boot Entry Point
├── config/
│   └── DataLoader.java                   // Seeds Admin, Students, Questions & Drives
├── exception/
│   ├── GlobalExceptionHandler.java       // Centralized @RestControllerAdvice
│   ├── ResourceNotFoundException.java    // Custom 404 Exception
│   └── ValidationException.java          // Custom 400 Exception
├── model/
│   ├── Evaluatable.java                  // Interface for evaluation contract
│   ├── Scorable.java                     // Interface for score & letter grading
│   ├── Trackable.java                    // Interface for eligibility tracking
│   ├── User.java                         // Abstract base class (Encapsulation)
│   ├── Student.java                      // Subclass of User (Inheritance, Overriding)
│   ├── Admin.java                        // Subclass of User (Inheritance, Overriding)
│   ├── Question.java                     // Abstract base class (Evaluatable)
│   ├── AptitudeQuestion.java             // Subclass of Question (Polymorphism)
│   ├── LogicalQuestion.java              // Subclass of Question (Polymorphism)
│   ├── TechnicalQuestion.java            // Subclass of Question (Polymorphism)
│   ├── QuestionEntity.java               // JPA Entity with Factory Method
│   ├── MockTest.java                     // Encapsulates timed test session
│   ├── Result.java                       // JPA Entity & Scorable implementation
│   └── PlacementDrive.java               // JPA Entity & Trackable implementation (Overloading)
├── repository/
│   ├── AdminRepository.java              // JPA Data Access
│   ├── StudentRepository.java            // JPA Data Access
│   ├── QuestionRepository.java           // Random question querying
│   ├── ResultRepository.java             // Attempt history queries
│   └── PlacementDriveRepository.java     // Placement drive queries
├── service/
│   ├── AuthService.java                  // Student & Admin authentication
│   ├── QuestionService.java              // Question Bank management & practice
│   ├── MockTestService.java              // Timed test orchestration & grading
│   ├── ResultService.java                // Analytics & performance summary
│   └── EligibilityService.java           // Placement eligibility evaluation
└── controller/
    ├── AuthController.java               // /api/auth
    ├── QuestionController.java           // /api/questions
    ├── MockTestController.java           // /api/mocktest
    ├── ResultController.java             // /api/results
    ├── PlacementDriveController.java     // /api/drives
    └── AdminController.java              // /api/admin
```

---

## 🗄️ Database Setup & Configuration

The application supports both **MySQL** (for production / assignment submission) and **H2 in-memory database** (for instant viva demonstration with zero setup).

### Option A: Instant Viva Demo (Zero-Setup / Default)
By default, the application runs with an embedded H2 database in MySQL compatibility mode. On startup, `DataLoader.java` automatically seeds:
- 1 Placement Officer Admin (`admin@college.edu` / `admin123`)
- 3 Sample Students (`arun@college.edu`, `priya@college.edu`, `rohan@college.edu`)
- 16 Placement Assessment MCQs (Aptitude, Logical Reasoning, Technical Java & OOP)
- 5 Campus Placement Drives (Google, Microsoft, Amazon, TCS Digital, Cognizant)

H2 Web Console is available at:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:placementdb`
- **User**: `sa` | **Password**: *(leave blank)*

---

### Option B: Local MySQL Database Setup
If running with a local MySQL server:

1. **Create the Database and Tables using provided scripts**:
   Open MySQL Workbench or MySQL CLI:
   ```bash
   mysql -u root -p < src/main/resources/schema.sql
   mysql -u root -p < src/main/resources/data.sql
   ```

2. **Configure Database Credentials**:
   Edit `src/main/resources/application-mysql.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/placement_portal_db?createDatabaseIfNotExist=true&useSSL=false
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```

3. **Run with the MySQL Profile**:
   ```bash
   java -jar target/placement-portal-1.0.0.jar --spring.profiles.active=mysql
   ```

---

## 🚀 How to Run the Application

### Prerequisites
- **Java 17** or higher
- **Maven 3.8+**

### Step 1: Clone or Navigate to Project Directory
```bash
cd "oops project"
```

### Step 2: Build the Project
```bash
mvn clean package -DskipTests
```

### Step 3: Launch the Spring Boot Server
```bash
java -jar target/placement-portal-1.0.0.jar
```

Or using Maven:
```bash
mvn spring-boot:run
```

### Step 4: Open in Web Browser
Navigate to:
```
http://localhost:8080
```

---

## 🔑 Default Test Credentials

For quick viva demonstration, the login screen includes **1-click login buttons** for:

| Role | Name | Email | Password | Academic Profile |
| :--- | :--- | :--- | :--- | :--- |
| **Student** | Arun Kumar | `arun@college.edu` | `student123` | CGPA: 8.75 \| Backlogs: 0 \| High Readiness |
| **Student** | Priya Dharshini | `priya@college.edu` | `student123` | CGPA: 7.40 \| Backlogs: 1 \| Moderate Readiness |
| **Student** | Rohan Verma | `rohan@college.edu` | `student123` | CGPA: 6.20 \| Backlogs: 2 \| Initial Candidate |
| **Admin** | Prof. Ramesh Sharma | `admin@college.edu` | `admin123` | Training & Placement Officer (TPO) |

---

## 📡 REST API Reference

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new student candidate |
| `POST` | `/api/auth/login` | Authenticate student or admin |
| `GET` | `/api/auth/student/{id}` | Fetch current student profile |
| `GET` | `/api/questions/practice?category={CAT}` | Fetch practice questions by category |
| `POST` | `/api/questions/practice/evaluate` | Evaluates single practice answer instantly |
| `POST` | `/api/mocktest/start` | Launch a timed mock assessment session |
| `POST` | `/api/mocktest/submit` | Submit test answers, compute score, and save result |
| `GET` | `/api/results/student/{id}` | Fetch full test history for a student |
| `GET` | `/api/results/student/{id}/summary` | Retrieve performance summary analytics |
| `GET` | `/api/drives` | List all campus placement drives |
| `GET` | `/api/drives/eligibility/{studentId}` | Personalized candidate eligibility report |
| `POST` | `/api/drives/check-hypothetical` | Quick 'What-If' eligibility simulator |
| `GET` | `/api/admin/stats` | Admin overview metrics |
| `GET` | `/api/admin/questions` | List all questions in question bank |
| `POST` | `/api/admin/questions` | Add new question |
| `DELETE` | `/api/admin/questions/{id}` | Remove question from question bank |
| `POST` | `/api/admin/drives` | Add new placement drive |
| `DELETE` | `/api/admin/drives/{id}` | Remove placement drive |

---

## 📝 Viva Presentation Tips

When asked about the project during your college viva:
1. **Explain the Architecture First**: Mention that you built a 3-tier Spring Boot architecture with RESTful controllers, service interfaces, JPA repositories, and an interactive Single Page frontend.
2. **Highlight the Polymorphic Evaluation**: Point out that questions in the mock test are held as a collection of `Question` references. When the test is graded, `question.evaluateAnswer(...)` is dynamically dispatched to the specific subclass (`AptitudeQuestion`, `LogicalQuestion`, or `TechnicalQuestion`).
3. **Showcase Method Overloading**: Demonstrate how `PlacementDrive.isEligible` is overloaded to evaluate a full `Student` object or hypothetical `(cgpa, backlogs)` parameters in the What-If Simulator.
4. **Demonstrate Encapsulation**: Show the `Student` class where `setCgpa(...)` validates the `0.0 - 10.0` range and `student.updatePerformance(...)` encapsulates the running average calculation.
5. **Show Constructor Chaining**: Explain that child constructors invoke `super(...)` to ensure parent state (`User`, `Question`) is reliably initialized first.
