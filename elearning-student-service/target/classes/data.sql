-- Courses
INSERT INTO course (id, title, description) VALUES
(1, 'Spring Boot Basics', 'Learn Spring Boot fundamentals, building REST APIs and understanding dependency injection.'),
(2, 'Java Microservices', 'Intro to Microservices with Spring Boot, service discovery, and inter-service communication.'),
(3, 'Advanced Spring Boot', 'Deep dive into Spring Boot features, security, and performance tuning.'),
(4, 'Database with Java', 'Learn JDBC, JPA, and Hibernate for managing relational databases in Java applications.'),
(5, 'RESTful APIs with Spring', 'Design and implement RESTful APIs using Spring Boot, with authentication and validation.'),
(6, 'Spring Cloud Essentials', 'Explore Spring Cloud for distributed systems, configuration, and cloud-native patterns.');

-- Quizzes
INSERT INTO quiz (id, title, course_id) VALUES
(1, 'Quiz 1 - Spring Boot Basics', 1),
(2, 'Quiz 2 - Java Microservices', 2),
(3, 'Quiz 3 - Advanced Spring Boot', 3),
(4, 'Quiz 4 - Databases with Java', 4),
(5, 'Quiz 5 - RESTful APIs', 5),
(6, 'Quiz 6 - Spring Cloud', 6);

-- Optional: add some sample questions for each quiz
INSERT INTO question (id, quiz_id, question_text) VALUES
(1, 1, 'What is Spring Boot’s main advantage over traditional Spring?'),
(2, 1, 'Which annotation is used to mark a Spring Boot application class?'),
(3, 2, 'What is the difference between monolithic and microservices architecture?'),
(4, 2, 'Name two ways services can communicate in a microservices setup.'),
(5, 3, 'How do you enable Spring Boot Actuator in your project?'),
(6, 3, 'What is the purpose of @Transactional annotation in Spring?');
