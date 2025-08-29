package com.student.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.entity.Student;
import com.student.service.StudentService;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/public/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is running");
    }

    @GetMapping("/profile")
    public ResponseEntity<Student> getCurrentProfile(Authentication authentication) {
        
        Student student = studentService.syncUserFromKeycloak(authentication);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/profile")
    public ResponseEntity<Student> updateCurrentProfile(
            @RequestBody Student updatedStudent, 
            Authentication authentication) {
        Student currentUser = studentService.getCurrentUser(authentication);
        Student updated = studentService.updateStudent(currentUser.getId(), updatedStudent, authentication);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/admin/students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    

    @PutMapping("/admin/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id, 
            @RequestBody Student updatedStudent,
            Authentication authentication) {
        Student updated = studentService.updateStudent(id, updatedStudent, authentication);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/student/dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> studentDashboard(Authentication authentication) {
        Student student = studentService.getCurrentUser(authentication);
        return ResponseEntity.ok("Welcome to student dashboard, " + student.getFullName());
    }

    @GetMapping("/teacher/dashboard")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> teacherDashboard(Authentication authentication) {
        Student teacher = studentService.getCurrentUser(authentication);
        return ResponseEntity.ok("Welcome to teacher dashboard, " + teacher.getFullName());
    }


    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard(Authentication authentication) {
        Student admin = studentService.getCurrentUser(authentication);
        return ResponseEntity.ok("Welcome to admin dashboard, " + admin.getFullName());
    }

    @GetMapping("/auth/userinfo")
    public ResponseEntity<Object> getUserInfo(Authentication authentication) {
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            return ResponseEntity.ok(Map.of(
                "keycloakId", jwt.getSubject(),
                "username", jwt.getClaimAsString("preferred_username"),
                "email", jwt.getClaimAsString("email"),
                "firstName", jwt.getClaimAsString("given_name"),
                "lastName", jwt.getClaimAsString("family_name"),
                "roles", jwt.getClaimAsStringList("realm_access.roles")
            ));
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }
}