-- ===================================================================
-- Placement Preparation Portal - MySQL Sample Seed Data
-- ===================================================================

USE placement_portal_db;

-- Admins
INSERT IGNORE INTO admins (id, name, email, password, role, employee_id, department_managed)
VALUES (1, 'Prof. Ramesh Sharma', 'admin@college.edu', 'admin123', 'ADMIN', 'EMP-TPO-01', 'Training & Placement Cell');

-- Students
INSERT IGNORE INTO students (id, name, email, password, role, roll_number, department, cgpa, backlogs, tests_attempted, average_score)
VALUES 
(1, 'Arun Kumar', 'arun@college.edu', 'student123', 'STUDENT', '2024CS01', 'Computer Science & Engineering', 8.75, 0, 1, 85.0),
(2, 'Priya Dharshini', 'priya@college.edu', 'student123', 'STUDENT', '2024IT14', 'Information Technology', 7.40, 1, 1, 68.0),
(3, 'Rohan Verma', 'rohan@college.edu', 'student123', 'STUDENT', '2024EC22', 'Electronics & Communication', 6.20, 2, 0, 0.0);

-- Questions (Aptitude, Logical, Technical)
INSERT IGNORE INTO questions (id, category, sub_category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation, difficulty, marks)
VALUES
(1, 'APTITUDE', 'Percentages', 'If the price of petrol increases by 25%, by how much percentage must a person reduce consumption so that expenditure remains unchanged?', '20%', '25%', '16.66%', '30%', 'A', 'Reduction % = (25 / 125) * 100 = 20%.', 'MEDIUM', 1),
(2, 'APTITUDE', 'Time & Work', 'A can complete a project in 12 days and B can do it in 16 days. If they work together, how many days will it take to finish the work?', '6.85 days', '7 days', '5.5 days', '8 days', 'A', '1 day work = 1/12 + 1/16 = 7/48. Total time = 48/7 = 6.85 days.', 'MEDIUM', 1),
(3, 'APTITUDE', 'Speed, Time & Distance', 'A train 180 meters long is travelling at 54 km/hr. In how many seconds will it pass a telegraph post?', '10 seconds', '12 seconds', '15 seconds', '18 seconds', 'B', 'Speed = 54 * (5/18) = 15 m/s. Time = 180 / 15 = 12 seconds.', 'EASY', 1),
(4, 'APTITUDE', 'Probability', 'Two unbiased dice are rolled together. What is the probability of getting a sum of 8?', '5/36', '1/6', '7/36', '1/9', 'A', 'Outcomes: (2,6), (3,5), (4,4), (5,3), (6,2) -> 5/36.', 'MEDIUM', 1),
(5, 'APTITUDE', 'Profit & Loss', 'A shopkeeper marks an item 40% above the cost price and allows a discount of 20%. What is his profit percentage?', '12%', '15%', '18%', '20%', 'A', 'SP = 140 * 0.80 = 112. Profit = 12%.', 'EASY', 1),

(6, 'LOGICAL', 'Number Series', 'Find the next number in the sequence: 4, 9, 25, 49, 121, ?', '144', '169', '196', '225', 'B', 'Squares of consecutive primes: 2^2, 3^2, 5^2, 7^2, 11^2, 13^2 = 169.', 'MEDIUM', 1),
(7, 'LOGICAL', 'Blood Relations', 'Pointing to a photograph, a woman says: "He is the only son of the father of my brother." How is the man related to the woman?', 'Father', 'Brother', 'Uncle', 'Cousin', 'B', 'Father of brother is father. Only son is brother.', 'EASY', 1),
(8, 'LOGICAL', 'Coding-Decoding', 'In a certain code, SPRING is written as UNTRPK. How will SUMMER be written in that code?', 'UWOOGT', 'UWOPHT', 'UXOOGT', 'VWPOGT', 'A', 'Shift each letter by +2.', 'EASY', 1),
(9, 'LOGICAL', 'Direction Sense', 'A person walks 5 km North, turns right and walks 4 km, then turns right and walks 5 km. In which direction and distance is he from the starting point?', '4 km East', '4 km West', '5 km East', '5 km South', 'A', '4 km East of the starting point.', 'EASY', 1),
(10, 'LOGICAL', 'Syllogism', 'Statements: Some pens are books. All books are papers. Conclusions: I. Some pens are papers. II. No pen is paper.', 'Only I follows', 'Only II follows', 'Either I or II follows', 'Neither follows', 'A', 'Only conclusion I definitely follows.', 'MEDIUM', 1),

(11, 'TECHNICAL', 'Java OOP', 'Which Java feature allows a subclass to provide a specific implementation of a method already declared in its parent class?', 'Method Overloading', 'Method Overriding', 'Abstraction', 'Encapsulation', 'B', 'Method Overriding allows child class to provide a specific implementation of an inherited method.', 'EASY', 1),
(12, 'TECHNICAL', 'Java Memory & JVM', 'Where are Java object instances stored in memory during application runtime?', 'Stack memory', 'Heap memory', 'PermGen/Metaspace only', 'CPU Registers', 'B', 'All Java objects are allocated dynamically on the Heap.', 'EASY', 1),
(13, 'TECHNICAL', 'Interfaces & Abstraction', 'Can an abstract class in Java have a constructor?', 'No, constructors cannot exist in abstract classes', 'Yes, but it can only be invoked by subclass constructors using super()', 'Yes, and it can be instantiated directly using new AbstractClass()', 'Only if all methods are abstract', 'B', 'Abstract classes can have constructors called via super() during subclass instantiation.', 'MEDIUM', 1),
(14, 'TECHNICAL', 'Polymorphism', 'What enables Runtime Polymorphism (Dynamic Method Dispatch) in Java?', 'Method Overloading with varying argument counts', 'Method Overriding resolved at runtime based on the actual object type', 'Static keyword binding at compile time', 'Final methods in parent classes', 'B', 'Dynamic Method Dispatch resolves overridden methods at runtime based on the actual object referenced.', 'MEDIUM', 1),
(15, 'TECHNICAL', 'Encapsulation', 'How is Encapsulation best achieved in a Java class?', 'By making all variables public for direct accessibility', 'By declaring fields private and providing public getters and setters with validation', 'By implementing multiple interfaces', 'By extending abstract classes', 'B', 'Encapsulation is achieved through private fields and public getter/setter methods.', 'EASY', 1),
(16, 'TECHNICAL', 'Exception Handling', 'Which block in a Java try-catch-finally construct is guaranteed to execute regardless of whether an exception was thrown or caught?', 'try block', 'catch block', 'finally block', 'throw block', 'C', 'The finally block always executes for deterministic resource cleanup.', 'EASY', 1);

-- Placement Drives
INSERT IGNORE INTO placement_drives (id, company_name, job_role, package_lpa, min_cgpa, max_backlogs, min_test_score, drive_date, status, job_description)
VALUES
(1, 'Google', 'Software Engineer (L3)', 28.5, 8.50, 0, 80.0, '2026-11-15', 'ACTIVE', 'Role involves building large-scale distributed systems and microservices.'),
(2, 'Microsoft', 'Software Development Engineer (SDE-1)', 22.0, 8.00, 0, 75.0, '2026-11-20', 'ACTIVE', 'Core engineering role across Azure, Windows, and AI services.'),
(3, 'Amazon', 'Associate Software Engineer', 18.5, 7.50, 0, 70.0, '2026-12-05', 'UPCOMING', 'E-commerce platform tools and AWS cloud systems.'),
(4, 'TCS Digital', 'Digital Systems Engineer', 7.5, 6.50, 1, 60.0, '2026-12-10', 'UPCOMING', 'Full-stack software development and enterprise digital solutions.'),
(5, 'Cognizant (GenC Elevate)', 'Software Analyst', 6.0, 6.00, 2, 50.0, '2026-12-18', 'UPCOMING', 'Java application maintenance, testing, and administration.');
