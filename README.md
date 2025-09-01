# E-Learning System

A simple **e-learning platform** built with a **microservices architecture**.  
The system supports **three roles**: **Student**, **Teacher**, and **Admin**.  
It demonstrates integration of authentication, inter-service communication, and database management.

---

## Features

- **Students**
  - Enroll in courses
  - Take quizzes and answer revision questions

- **Teachers**
  - Assigned to courses
  - Manage course content and quizzes

- **Admins**
  - Manage users, courses, and system operations

---

## Architecture Overview

- **Microservices** with gRPC for communication
- **PostgreSQL** as the database
- **Keycloak** for authentication & authorization
- **Spring Boot** (Java) with **Maven** for dependency management
- **Role-based access control** (Student, Teacher, Admin)
- **Deploy** using Docker Compose

---

## Tech Stack

- **Backend**: Java, Spring Boot, gRPC  
- **Database**: PostgreSQL  
- **Authentication**: Keycloak  
- **Build Tool**: Maven  
- **Containerization**: Docker Compose

---

## Getting Started

### 1. Clone the repository
```bash
git clone [https://github.com/yourusername/e-learning-system.git](https://github.com/trungmac07/E-learning-System.git)
cd e-learning-system
mvn clean install
docker-compose up --build
```
### 2. Access KeyCloak
URL: http://localhost:9000

Default realm: e-learning

Roles: student, teacher, admin

### 3.Authentication
Authentication & Roles

Keycloak issues JWT tokens.

Each service validates tokens and enforces role-based access.

## APIs
### API Endpoints – User Service

> All endpoints are prefixed with `/api`.  
> Authentication is handled via **Keycloak JWT tokens**.  
> Access is controlled using role-based authorization.  

##### 🔹 Public

| Method | Endpoint           | Role(s) | Description              |
|--------|--------------------|---------|--------------------------|
| GET    | `/public/health`   | Public  | Health check of service. |

---

##### 🔹 User Profile

| Method | Endpoint      | Role(s)                  | Description                   |
|--------|---------------|--------------------------|-------------------------------|
| GET    | `/profile`    | Student, Teacher, Admin  | Get current user profile.     |
| PUT    | `/profile`    | Student, Teacher, Admin  | Update current user profile.  |

---

##### 🔹 Students

| Method | Endpoint              | Role(s)            | Description                                |
|--------|-----------------------|--------------------|--------------------------------------------|
| GET    | `/students`           | Admin, Teacher     | Get all students (with optional filters).  |
| GET    | `/students/{id}`      | Admin, Teacher     | Get a student by ID.                       |
| PUT    | `/students/{id}`      | Admin              | Update a student.                          |

---

##### 🔹 Teachers

| Method | Endpoint              | Role(s) | Description                                |
|--------|-----------------------|---------|--------------------------------------------|
| GET    | `/teachers`           | Admin   | Get all teachers (with optional filters).  |
| GET    | `/teachers/{id}`      | Admin   | Get a teacher by ID.                       |
| PUT    | `/teachers/{id}`      | Admin   | Update a teacher.                          |

---

##### 🔹 Admins

| Method | Endpoint          | Role(s) | Description              |
|--------|-------------------|---------|--------------------------|
| GET    | `/admins`         | Admin   | Get all admins.          |
| GET    | `/admins/{id}`    | Admin   | Get an admin by ID.      |

---

##### 🔹 Dashboards

| Method | Endpoint              | Role(s)  | Description                        |
|--------|-----------------------|----------|------------------------------------|
| GET    | `/student/dashboard`  | Student  | Get student dashboard data.        |
| GET    | `/teacher/dashboard`  | Teacher  | Get teacher dashboard data.        |
| GET    | `/admin/dashboard`    | Admin    | Get admin dashboard data.          |

---

##### 🔹 Statistics

| Method | Endpoint         | Role(s) | Description                                |
|--------|------------------|---------|--------------------------------------------|
| GET    | `/admin/stats`   | Admin   | Get system-wide statistics (users, roles). |

---

##### 🔹 Auth Info

| Method | Endpoint         | Role(s) | Description                               |
|--------|------------------|---------|-------------------------------------------|
| GET    | `/auth/userinfo` | Any     | Get current user info from Keycloak + DB. |

## API Endpoints – Course Service

> All endpoints are prefixed with `/api/courses`.  
> Authentication is handled via **Keycloak JWT tokens**.  

##### 🔹 Courses

| Method | Endpoint            | Role(s)            | Description                |
|--------|---------------------|--------------------|----------------------------|
| POST   | `/`                 | Admin              | Create a new course.       |
| GET    | `/`                 | Any (authenticated)| Get all courses.           |
| GET    | `/{id}`             | Any (authenticated)| Get a course by ID.        |
| DELETE | `/{id}`             | Admin              | Delete a course by ID.     |

---

##### 🔹 Quizzes

| Method | Endpoint                      | Role(s)              | Description                       |
|--------|-------------------------------|----------------------|-----------------------------------|
| POST   | `/{courseId}/quizzes`         | Admin, Teacher       | Add a quiz to a course.           |
| GET    | `/{courseId}/quizzes`         | Any (authenticated)  | Get quizzes for a course.         |

---

##### 🔹 Questions

| Method | Endpoint                           | Role(s)             | Description                       |
|--------|------------------------------------|---------------------|-----------------------------------|
| POST   | `/quizzes/{quizId}/questions`      | Any (authenticated) | Add a question to a quiz.         |
| GET    | `/quizzes/{quizId}/questions`      | Any (authenticated) | Get questions for a quiz.         |

### API Endpoints – Enrollment Service

> All endpoints are prefixed with `/api/enrollment`.  
> Authentication is handled via **Keycloak JWT tokens**.  

#### 🔹 Enrollment

| Method | Endpoint   | Role(s)            | Description                          |
|--------|------------|--------------------|--------------------------------------|
| POST   | `/`        | Admin, Student     | Enroll a student into a course.      |
| DELETE | `/`        | Admin, Student     | Unenroll a student from a course.    |

### API Endpoints – Assignment Service

> All endpoints are prefixed with `/api/assignment`.  
> Authentication is handled via **Keycloak JWT tokens**.  

#### 🔹 Teacher Assignment

| Method | Endpoint   | Role(s) | Description                       |
|--------|------------|---------|-----------------------------------|
| POST   | `/`        | Admin   | Assign a teacher to a course.     |
| DELETE | `/`        | Admin   | Unassign a teacher from a course. |
