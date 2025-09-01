package com.user.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.dto.request.UpdateTeacherRequest;
import com.user.dto.request.UpdateUserRequest;
import com.user.dto.response.UserProfileResponse;
import com.user.entity.Admin;
import com.user.entity.Student;
import com.user.entity.Teacher;
import com.user.entity.User;
import com.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // PUBLIC ENDPOINTS 

    @GetMapping("/public/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "User Service",
            "timestamp", java.time.Instant.now().toString()
        ));
    }

    // USER PROFILE ENDPOINTS 

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getCurrentProfile(Authentication authentication) {
        User user = userService.syncUserFromKeycloak(authentication);
        UserProfileResponse response = buildUserProfileResponse(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateCurrentProfile(
            @Valid @RequestBody UpdateUserRequest updateRequest, 
            Authentication authentication) {
        
        User currentUser = userService.getCurrentUser(authentication);
        User updatedUser = userService.updateUser(currentUser.getId(), 
            mapToUserEntity(updateRequest), authentication);
        
        UserProfileResponse response = buildUserProfileResponse(updatedUser);
        return ResponseEntity.ok(response);
    }

    // STUDENT ENDPOINTS 

    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Student>> getAllStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean gender) {
        
        List<Student> students;
        
        if (search != null && !search.trim().isEmpty()) {
            students = userService.findStudentsByName(search.trim());
        } else if (gender != null) {
            students = userService.findStudentsByGender(gender);
        } else {
            students = userService.getAllStudents();
        }
        
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return userService.findStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateUserRequest updateRequest,
            Authentication authentication) {
        
        Student updated = userService.updateStudent(id, 
            mapToStudentEntity(updateRequest), authentication);
        return ResponseEntity.ok(updated);
    }

    // TEACHER ENDPOINTS 

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Teacher>> getAllTeachers(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String search) {
        
        List<Teacher> teachers;
        
        if (department != null && !department.trim().isEmpty()) {
            teachers = userService.findTeachersByDepartment(department.trim());
        } else if (search != null && !search.trim().isEmpty()) {
            teachers = userService.findTeachersByName(search.trim());
        } else {
            teachers = userService.getAllTeachers();
        }
        
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/teachers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) 
    {
        return userService.findTeacherById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/teachers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Teacher> updateTeacher(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateTeacherRequest updateRequest,
            Authentication authentication) {
        
        Teacher updated = userService.updateTeacher(id, 
            mapToTeacherEntity(updateRequest), authentication);
        return ResponseEntity.ok(updated);
    }

    // ADMIN ENDPOINTS 

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = userService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/admins/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return userService.findAdminById(id) 
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    // DASHBOARD ENDPOINTS 

    @GetMapping("/student/dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> studentDashboard(Authentication authentication) {
        Student student = userService.getCurrentStudent(authentication);
        
        Map<String, Object> dashboardData = Map.of(
            "message", "Welcome to student dashboard, " + student.getUser().getFullName(),
            "user", student,
            "role", "STUDENT",
            "permissions", List.of("VIEW_COURSES", "ENROLL_COURSES", "VIEW_GRADES")
        );
        
        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/teacher/dashboard")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> teacherDashboard(Authentication authentication) {
        Teacher teacher = userService.getCurrentTeacher(authentication);
        
        Map<String, Object> dashboardData = Map.of(
            "message", "Welcome to teacher dashboard, " + teacher.getUser().getFullName(),
            "user", teacher,
            "role", "TEACHER",
            "department", teacher.getDepartment() != null ? teacher.getDepartment() : "Not Assigned",
            "permissions", List.of("VIEW_STUDENTS", "MANAGE_COURSES", "GRADE_ASSIGNMENTS")
        );
        
        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> adminDashboard(Authentication authentication) {
        Admin admin = userService.getCurrentAdmin(authentication);
        
        Map<String, Object> dashboardData = Map.of(
            "message", "Welcome to admin dashboard, " + admin.getUser().getFullName(),
            "user", admin,
            "role", "ADMIN",
            "permissions", List.of("MANAGE_USERS", "MANAGE_COURSES", "VIEW_REPORTS", "SYSTEM_CONFIG")
        );
        
        return ResponseEntity.ok(dashboardData);
    }

    // STATISTICS ENDPOINTS 

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        Map<String, Object> stats = Map.of(
            "totalStudents", userService.countStudents(),
            "totalTeachers", userService.countTeachers(),
            "totalAdmins", userService.countAdmins(),
            "totalUsers", userService.countAllUsers()
        );
        
        return ResponseEntity.ok(stats);
    }

    // AUTH INFO ENDPOINT 

    @GetMapping("/auth/userinfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            
            // Also sync/get user from local database
            User localUser = userService.syncUserFromKeycloak(authentication);
            
            return ResponseEntity.ok(Map.of(
                "keycloakId", jwt.getSubject(),
                "username", jwt.getClaimAsString("preferred_username"),
                "email", jwt.getClaimAsString("email"),
                "firstName", jwt.getClaimAsString("given_name"),
                "lastName", jwt.getClaimAsString("family_name"),
                "roles", jwt.getClaimAsStringList("realm_access.roles"),
                "localUser", Map.of(
                    "id", localUser.getId(),
                    "role", localUser.getRole(),
                    "fullName", localUser.getFullName(),
                    "gender", localUser.getGender()
                )
            ));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid token"));
    }

    // HELPER METHODS 

    private UserProfileResponse buildUserProfileResponse(User user) {
        return UserProfileResponse.builder()
            .id(user.getId())
            .keycloakId(user.getKeycloakId())
            .username(user.getUsername())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .role(user.getRole())
            .gender(user.getGender())
            .build();
    }

    private User mapToUserEntity(UpdateUserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setGender(request.getGender());
        return user;
    }

    private Student mapToStudentEntity(UpdateUserRequest request) {
        Student student = new Student();
        User user = new User();
        user.setFullName(request.getFullName());
        user.setGender(request.getGender());
        student.setUser(user);
        return student;
    }

    private Teacher mapToTeacherEntity(UpdateTeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setDepartment(request.getDepartment());
        
        User user = new User();
        user.setFullName(request.getFullName());
        user.setGender(request.getGender());
        teacher.setUser(user);
        
        return teacher;
    }
}
