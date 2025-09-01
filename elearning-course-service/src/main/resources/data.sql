-- Courses
INSERT INTO course (id, title, description) VALUES
(1, 'Spring Boot Basics', 'Learn Spring Boot fundamentals, building REST APIs and understanding dependency injection.'),
(2, 'Java Microservices', 'Intro to Microservices with Spring Boot, service discovery, and inter-service communication.'),
(3, 'Advanced Spring Boot', 'Deep dive into Spring Boot features, security, and performance tuning.'),
(4, 'Database with Java', 'Learn JDBC, JPA, and Hibernate for managing relational databases in Java applications.'),
(5, 'RESTful APIs with Spring', 'Design and implement RESTful APIs using Spring Boot, with authentication and validation.'),
(6, 'Spring Cloud Essentials', 'Explore Spring Cloud for distributed systems, configuration, and cloud-native patterns.'),
(7, 'Spring Security Fundamentals', 'Understand Spring Security, authentication, authorization, and JWT integration.'),
(8, 'Testing in Spring', 'Learn unit testing, integration testing, and Testcontainers with Spring Boot.'),
(9, 'Reactive Programming with Spring WebFlux', 'Introduction to reactive programming concepts and Spring WebFlux framework.'),
(10, 'Deploying Spring Applications', 'Best practices for deploying Spring apps with Docker, Kubernetes, and CI/CD.');

-- Quizzes
INSERT INTO quiz (id, title, course_id) VALUES
(1, 'Quiz 1 - Spring Boot Basics', 1),
(2, 'Quiz 2 - Java Microservices', 2),
(3, 'Quiz 3 - Advanced Spring Boot', 3),
(4, 'Quiz 4 - Databases with Java', 4),
(5, 'Quiz 5 - RESTful APIs', 5),
(6, 'Quiz 6 - Spring Cloud', 6),
(7, 'Quiz 7 - Spring Security', 7),
(8, 'Quiz 8 - Testing with Spring', 8),
(9, 'Quiz 9 - Spring WebFlux', 9),
(10, 'Quiz 10 - Deployment & CI/CD', 10);

-- Questions (3–5 per quiz)
INSERT INTO question (id, quiz_id, question_text) VALUES
-- Spring Boot Basics
(1, 1, 'What is Spring Boot’s main advantage over traditional Spring?'),
(2, 1, 'Which annotation is used to mark a Spring Boot application class?'),
(3, 1, 'What is the default embedded server used by Spring Boot?'),
(4, 1, 'How can you override application properties in Spring Boot?'),

-- Java Microservices
(5, 2, 'What is the difference between monolithic and microservices architecture?'),
(6, 2, 'Name two ways services can communicate in a microservices setup.'),
(7, 2, 'What is the role of Eureka in Spring Cloud?'),
(8, 2, 'Why is API Gateway important in microservices?'),

-- Advanced Spring Boot
(9, 3, 'How do you enable Spring Boot Actuator in your project?'),
(10, 3, 'What is the purpose of @Transactional annotation in Spring?'),
(11, 3, 'How do you configure custom health checks in Actuator?'),
(12, 3, 'Explain the difference between lazy and eager initialization in Spring.'),

-- Database with Java
(13, 4, 'What is the difference between JDBC and JPA?'),
(14, 4, 'How does Hibernate manage entity states (transient, persistent, detached)?'),
(15, 4, 'What annotation is used to map a Java class to a database table?'),
(16, 4, 'What is the N+1 query problem and how can it be avoided?'),

-- RESTful APIs
(17, 5, 'Which HTTP methods are typically used in REST?'),
(18, 5, 'What is the difference between PUT and PATCH requests?'),
(19, 5, 'How do you secure a REST API in Spring Boot?'),
(20, 5, 'What is HATEOAS in REST APIs?'),

-- Spring Cloud
(21, 6, 'What is the purpose of Spring Cloud Config Server?'),
(22, 6, 'How does service discovery work in Spring Cloud?'),
(23, 6, 'What is circuit breaker pattern and which Spring Cloud project supports it?'),
(24, 6, 'Explain the difference between client-side and server-side load balancing.'),

-- Spring Security
(25, 7, 'What is the default login endpoint provided by Spring Security?'),
(26, 7, 'How do you configure role-based access in Spring Security?'),
(27, 7, 'What is the difference between authentication and authorization?'),
(28, 7, 'How do you implement JWT authentication in Spring Security?'),

-- Testing
(29, 8, 'What is the difference between unit testing and integration testing?'),
(30, 8, 'Which testing library is commonly used with Spring Boot for unit testing?'),
(31, 8, 'What does @SpringBootTest annotation do?'),
(32, 8, 'How can Testcontainers be used with Spring Boot tests?'),

-- WebFlux
(33, 9, 'What is the main difference between Spring MVC and WebFlux?'),
(34, 9, 'What is a Mono and a Flux in reactive programming?'),
(35, 9, 'Which HTTP server does WebFlux use by default?'),
(36, 9, 'Why is backpressure important in reactive streams?'),

-- Deployment
(37, 10, 'What is the purpose of a Dockerfile in Spring Boot deployment?'),
(38, 10, 'How can you create a Docker image for a Spring Boot app?'),
(39, 10, 'What are the advantages of deploying to Kubernetes?'),
(40, 10, 'How does CI/CD improve Spring application delivery?');
