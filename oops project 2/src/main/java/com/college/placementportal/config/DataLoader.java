package com.college.placementportal.config;

import com.college.placementportal.model.Admin;
import com.college.placementportal.model.PlacementDrive;
import com.college.placementportal.model.QuestionEntity;
import com.college.placementportal.model.Student;
import com.college.placementportal.repository.AdminRepository;
import com.college.placementportal.repository.PlacementDriveRepository;
import com.college.placementportal.repository.QuestionRepository;
import com.college.placementportal.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final PlacementDriveRepository driveRepository;

    public DataLoader(AdminRepository adminRepository,
                      StudentRepository studentRepository,
                      QuestionRepository questionRepository,
                      PlacementDriveRepository driveRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
        this.driveRepository = driveRepository;
    }

    @Override
    public void run(String... args) {
        seedAdmins();
        seedStudents();
        seedQuestions();
        seedPlacementDrives();
    }

    private void seedAdmins() {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin(
                    "Prof. Ramesh Sharma",
                    "admin@college.edu",
                    "admin123",
                    "EMP-TPO-01",
                    "Training & Placement Cell"
            );
            adminRepository.save(admin);
        }
    }

    private void seedStudents() {
        if (studentRepository.count() == 0) {
            Student s1 = new Student(
                    "Arun Kumar",
                    "arun@college.edu",
                    "student123",
                    "2024CS01",
                    "Computer Science & Engineering",
                    8.75,
                    0
            );
            s1.updatePerformance(85.0);

            Student s2 = new Student(
                    "Priya Dharshini",
                    "priya@college.edu",
                    "student123",
                    "2024IT14",
                    "Information Technology",
                    7.40,
                    1
            );
            s2.updatePerformance(68.0);

            Student s3 = new Student(
                    "Rohan Verma",
                    "rohan@college.edu",
                    "student123",
                    "2024EC22",
                    "Electronics & Communication",
                    6.20,
                    2
            );

            studentRepository.save(s1);
            studentRepository.save(s2);
            studentRepository.save(s3);
        }
    }

    private void seedQuestions() {
        if (questionRepository.count() == 0) {
            List<QuestionEntity> list = new ArrayList<>();

            // ==================== APTITUDE QUESTIONS ====================
            list.add(new QuestionEntity(
                    "APTITUDE", "Percentages",
                    "If the price of petrol increases by 25%, by how much percentage must a person reduce consumption so that expenditure remains unchanged?",
                    "20%", "25%", "16.66%", "30%",
                    "A",
                    "Reduction % = (r / (100 + r)) * 100 = (25 / 125) * 100 = 20%.",
                    "MEDIUM", 1
            ));

            list.add(new QuestionEntity(
                    "APTITUDE", "Time & Work",
                    "A can complete a project in 12 days and B can do it in 16 days. If they work together, how many days will it take to finish the work?",
                    "6.85 days", "7 days", "5.5 days", "8 days",
                    "A",
                    "1 day work = 1/12 + 1/16 = 7/48. Total time = 48/7 = 6.85 days.",
                    "MEDIUM", 1
            ));

            list.add(new QuestionEntity(
                    "APTITUDE", "Speed, Time & Distance",
                    "A train 180 meters long is travelling at 54 km/hr. In how many seconds will it pass a telegraph post?",
                    "10 seconds", "12 seconds", "15 seconds", "18 seconds",
                    "B",
                    "Speed = 54 * (5/18) = 15 m/s. Time = Distance / Speed = 180 / 15 = 12 seconds.",
                    "EASY", 1
            ));

            list.add(new QuestionEntity(
                    "APTITUDE", "Probability",
                    "Two unbiased dice are rolled together. What is the probability of getting a sum of 8?",
                    "5/36", "1/6", "7/36", "1/9",
                    "A",
                    "Favorable outcomes for sum 8: (2,6), (3,5), (4,4), (5,3), (6,2) -> 5 outcomes. Total = 36. Probability = 5/36.",
                    "MEDIUM", 1
            ));

            list.add(new QuestionEntity(
                    "APTITUDE", "Profit & Loss",
                    "A shopkeeper marks an item 40% above the cost price and allows a discount of 20%. What is his profit percentage?",
                    "12%", "15%", "18%", "20%",
                    "A",
                    "Let CP = 100. MP = 140. SP = 140 * 0.80 = 112. Profit = 112 - 100 = 12%.",
                    "EASY", 1
            ));

            // ==================== LOGICAL REASONING ====================
            list.add(new QuestionEntity(
                    "LOGICAL", "Number Series",
                    "Find the next number in the sequence: 4, 9, 25, 49, 121, ?",
                    "144", "169", "196", "225",
                    "B",
                    "The series consists of squares of consecutive prime numbers: 2^2=4, 3^2=9, 5^2=25, 7^2=49, 11^2=121, 13^2=169.",
                    "MEDIUM", 1
            ));

            list.add(new QuestionEntity(
                    "LOGICAL", "Blood Relations",
                    "Pointing to a photograph, a woman says: 'He is the only son of the father of my brother.' How is the man in the photo related to the woman?",
                    "Father", "Brother", "Uncle", "Cousin",
                    "B",
                    "Father of my brother is my father. Only son of my father is my brother (or myself if male; since speaker is female, it is her brother).",
                    "EASY", 1
            ));

            list.add(new QuestionEntity(
                    "LOGICAL", "Coding-Decoding",
                    "In a certain code, 'SPRING' is written as 'UNTRPK'. How will 'SUMMER' be written in that code?",
                    "UWOOGT", "UWOPHT", "UXOOGT", "VWPOGT",
                    "A",
                    "Each letter is shifted by +2: S->U, U->W, M->O, M->O, E->G, R->T = UWOOGT.",
                    "EASY", 1
            ));

            list.add(new QuestionEntity(
                    "LOGICAL", "Direction Sense",
                    "A person walks 5 km North, turns right and walks 4 km, then turns right and walks 5 km. In which direction and distance is he from the starting point?",
                    "4 km East", "4 km West", "5 km East", "5 km South",
                    "A",
                    "North 5km, East 4km, South 5km returns to horizontal line 4 km East from starting position.",
                    "EASY", 1
            ));

            list.add(new QuestionEntity(
                    "LOGICAL", "Syllogism",
                    "Statements: Some pens are books. All books are papers. Conclusions: I. Some pens are papers. II. No pen is paper.",
                    "Only I follows", "Only II follows", "Either I or II follows", "Neither follows",
                    "A",
                    "Pens overlap books, and books are contained in papers. Thus, some pens are papers. Only conclusion I definitely follows.",
                    "MEDIUM", 1
            ));

            // ==================== TECHNICAL JAVA & OOP ====================
            list.add(new QuestionEntity(
                    "TECHNICAL", "Java OOP",
                    "Which Java feature allows a subclass to provide a specific implementation of a method already declared in its parent class?",
                    "Method Overloading", "Method Overriding", "Abstraction", "Encapsulation",
                    "B",
                    "Method Overriding occurs when a child class defines a method with the same name, return type, and parameters as a method in its superclass.",
                    "EASY", 1
            ));

            list.add(new QuestionEntity(
                    "TECHNICAL", "Java Memory & JVM",
                    "Where are Java object instances stored in memory during application runtime?",
                    "Stack memory", "Heap memory", "PermGen/Metaspace only", "CPU Registers",
                    "B",
                    "All objects and arrays in Java are dynamically allocated on the Heap memory. References to them reside on the Stack.",
                    "EASY", 1
            ));

            list.add(new QuestionEntity(
                    "TECHNICAL", "Interfaces & Abstraction",
                    "Can an abstract class in Java have a constructor?",
                    "No, constructors cannot exist in abstract classes",
                    "Yes, but it can only be invoked by subclass constructors using super()",
                    "Yes, and it can be instantiated directly using new AbstractClass()",
                    "Only if all methods are abstract",
                    "B",
                    "Abstract classes can define constructors to initialize state; they are called during subclass constructor chaining via super().",
                    "MEDIUM", 1
            ));

            list.add(new QuestionEntity(
                    "TECHNICAL", "Polymorphism",
                    "What enables Runtime Polymorphism (Dynamic Method Dispatch) in Java?",
                    "Method Overloading with varying argument counts",
                    "Method Overriding resolved at runtime based on the actual object type",
                    "Static keyword binding at compile time",
                    "Final methods in parent classes",
                    "B",
                    "Dynamic Method Dispatch resolves overridden methods at runtime based on the actual object referenced.",
                    "MEDIUM", 1
            ));

            list.add(new QuestionEntity(
                    "TECHNICAL", "Encapsulation",
                    "How is Encapsulation best achieved in a Java class?",
                    "By making all variables public for direct accessibility",
                    "By declaring fields private and providing public getters and setters with validation",
                    "By implementing multiple interfaces",
                    "By extending abstract classes",
                    "B",
                    "Encapsulation packages data and methods into a single unit, hiding internal data with private access and exposing controlled getters/setters.",
                    "EASY", 1
            ));

            list.add(new QuestionEntity(
                    "TECHNICAL", "Exception Handling",
                    "Which block in a Java try-catch-finally construct is guaranteed to execute regardless of whether an exception was thrown or caught?",
                    "try block", "catch block", "finally block", "throw block",
                    "C",
                    "The finally block always executes (unless System.exit() is invoked or JVM crashes), making it ideal for resource cleanup.",
                    "EASY", 1
            ));

            questionRepository.saveAll(list);
        }
    }

    private void seedPlacementDrives() {
        if (driveRepository.count() == 0) {
            List<PlacementDrive> drives = new ArrayList<>();

            drives.add(new PlacementDrive(
                    "Google",
                    "Software Engineer (L3)",
                    28.5,
                    8.50,
                    0,
                    80.0,
                    "2026-11-15",
                    "ACTIVE",
                    "Role involves building large-scale distributed systems, algorithms, backend microservices and cloud infrastructure."
            ));

            drives.add(new PlacementDrive(
                    "Microsoft",
                    "Software Development Engineer (SDE-1)",
                    22.0,
                    8.00,
                    0,
                    75.0,
                    "2026-11-20",
                    "ACTIVE",
                    "Core engineering role across Azure, Microsoft 365, and AI platform services. Solid Java / C++ and OOP fundamentals required."
            ));

            drives.add(new PlacementDrive(
                    "Amazon",
                    "Associate Software Engineer",
                    18.5,
                    7.50,
                    0,
                    70.0,
                    "2026-12-05",
                    "UPCOMING",
                    "Building robust e-commerce customer experiences and AWS infrastructure tools. Focus on data structures, OOP design and problem solving."
            ));

            drives.add(new PlacementDrive(
                    "TCS Digital",
                    "Digital Systems Engineer",
                    7.5,
                    6.50,
                    1,
                    60.0,
                    "2026-12-10",
                    "UPCOMING",
                    "Full-stack development, modern web applications, and enterprise digital solutions."
            ));

            drives.add(new PlacementDrive(
                    "Cognizant (GenC Elevate)",
                    "Software Analyst",
                    6.0,
                    6.00,
                    2,
                    50.0,
                    "2026-12-18",
                    "UPCOMING",
                    "Java application maintenance, QA automation, and database administration."
            ));

            driveRepository.saveAll(drives);
        }
    }
}
