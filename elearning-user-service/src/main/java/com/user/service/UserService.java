package com.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.user.entity.Admin;
import com.user.entity.Student;
import com.user.entity.Teacher;
import com.user.entity.User;
import com.user.repository.AdminRepository;
import com.user.repository.StudentRepository;
import com.user.repository.TeacherRepository;
import com.user.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    public User syncUserFromKeycloak(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Invalid authentication token");
        }
        
        String keycloakId = jwt.getSubject(); 
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        String role = extractPrimaryRole(jwt);
        
        // Find existing user by keycloakId
        Optional<User> existingUser = userRepository.findByKeycloakId(keycloakId);
        
        User user;
        if (existingUser.isPresent()) {
            // Update existing user
            user = existingUser.get();
            updateUserFields(user, username, email, firstName, lastName, role);
        } else {
            // Create new user
            user = createNewUser(keycloakId, username, email, firstName, lastName, role);
        }
        
        user = userRepository.save(user);
        
        // Create or update role-specific profile
        createOrUpdateProfile(user);
        
        return user;
    }

    private void createOrUpdateProfile(User user) {
        switch (user.getRole()) {
            case "student" -> createOrUpdateStudent(user);
            case "teacher" -> createOrUpdateTeacher(user);
            case "admin" -> createOrUpdateAdmin(user);
        }
    }

    private void updateUserFields(User user, String username, String email, 
                                String firstName, String lastName, String role) {
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(buildFullName(firstName, lastName));
        user.setRole(role);
    }

    private User createNewUser(String keycloakId, String username, String email, 
                                String firstName, String lastName, String role) {
        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(buildFullName(firstName, lastName));
        user.setRole(role);
        return user;
    }

    private void createOrUpdateStudent(User user) {
        // Check if student profile already exists
        Optional<Student> existing = studentRepository.findByUser_Id(user.getId());
        
        if (existing.isEmpty()) {
            Student student = new Student();
            student.setUser(user);
            studentRepository.save(student);
        }
    }

    private void createOrUpdateTeacher(User user) {
        // Check if teacher profile already exists
        Optional<Teacher> existing = teacherRepository.findByUser_Id(user.getId());
        
        if (existing.isEmpty()) {
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            // You can set default department or extract from JWT if available
            teacher.setDepartment("Not Assigned");
            teacherRepository.save(teacher);
        }
    }

    private void createOrUpdateAdmin(User user) {
        // Check if admin profile already exists
        Optional<Admin> existing = adminRepository.findByUser_Id(user.getId());
        
        if (existing.isEmpty()) {
            Admin admin = new Admin();
            admin.setUser(user);
            adminRepository.save(admin);
        }
    }

    public User getCurrentUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String keycloakId = jwt.getSubject();
            return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found in local database"));
        }
        throw new RuntimeException("Invalid authentication token");
    }

    // Role-specific methods
    public Student getCurrentStudent(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (!"student".equals(user.getRole())) {
            throw new RuntimeException("User is not a student");
        }
        return studentRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> new RuntimeException("Student profile not found"));
    }

    public Teacher getCurrentTeacher(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (!"teacher".equals(user.getRole())) {
            throw new RuntimeException("User is not a teacher");
        }
        return teacherRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> new RuntimeException("Teacher profile not found"));
    }

    public Admin getCurrentAdmin(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (!"admin".equals(user.getRole())) {
            throw new RuntimeException("User is not an admin");
        }
        return adminRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> new RuntimeException("Admin profile not found"));
    }

    // Find methods
    public Optional<User> findUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }

    public List<User> findUserByName(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name);
    }

    public List<Student> findStudentsByName(String name)
    {
        return studentRepository.findByUser_FullNameContainingIgnoreCase(name);
    }

    public Optional<Student> findStudentById(Long id)
    {
        return studentRepository.findByUser_Id(id);
    }

    public Optional<Teacher> findTeacherById(Long id)
    {
        return teacherRepository.findByUser_Id(id);
    }

    public Optional<Admin> findAdminById(Long id)
    {
        return adminRepository.findByUser_Id(id);
    }

    public Optional<Student> findStudentByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
            .filter(user -> "student".equals(user.getRole()))
            .flatMap(user -> studentRepository.findByUser_Id(user.getId()));
    }

    // Get all methods
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Update methods
    public User updateUser(Long userId, User updatedUser, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        
        // Check if user can update (self or admin)
        if (!currentUser.getId().equals(userId) && !"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("Access denied");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update allowed fields
        if (updatedUser.getFullName() != null) {
            user.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getGender() != null) {
            user.setGender(updatedUser.getGender());
        }
        // Add other updateable fields as needed
        
        return userRepository.save(user);
    }

    public Student updateStudent(Long studentId, Student updatedStudent, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Check if user can update (self or admin)
        if (!currentUser.getId().equals(student.getUser().getId()) && !"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("Access denied");
        }
        
        // Update User fields through the relationship
        User user = student.getUser();
        if (updatedStudent.getUser() != null) {
            if (updatedStudent.getUser().getFullName() != null) {
                user.setFullName(updatedStudent.getUser().getFullName());
            }
            if (updatedStudent.getUser().getGender() != null) {
                user.setGender(updatedStudent.getUser().getGender());
            }
        }
        
        userRepository.save(user);
        return studentRepository.save(student);
    }

    public Teacher updateTeacher(Long teacherId, Teacher updatedTeacher, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        // Check if user can update (self or admin)
        if (!currentUser.getId().equals(teacher.getUser().getId()) && !"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("Access denied");
        }
        
        // Update teacher-specific fields
        if (updatedTeacher.getDepartment() != null) {
            teacher.setDepartment(updatedTeacher.getDepartment());
        }
        
        // Update User fields if provided
        User user = teacher.getUser();
        if (updatedTeacher.getUser() != null) {
            if (updatedTeacher.getUser().getFullName() != null) {
                user.setFullName(updatedTeacher.getUser().getFullName());
            }
            if (updatedTeacher.getUser().getGender() != null) {
                user.setGender(updatedTeacher.getUser().getGender());
            }
        }
        
        userRepository.save(user);
        return teacherRepository.save(teacher);
    }

    // Helper methods
    private String extractPrimaryRole(Jwt jwt) {
        
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> realmRoles = (List<String>) realmAccess.get("roles");

        if (realmRoles == null || realmRoles.isEmpty()) {
            return "student"; 
        }
        
        // Priority order: admin > teacher > student
        if (realmRoles.contains("admin")) return "admin";
        if (realmRoles.contains("teacher")) return "teacher";
        if (realmRoles.contains("student")) return "student";
        
        return "student"; // Default
    }

    private String buildFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) return null;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    public List<Student> findStudentsByGender(Boolean gender) {
        return studentRepository.findByUser_Gender(gender);
    }

    public List<Teacher> findTeachersByDepartment(String department) {
        return teacherRepository.findByDepartment(department);
    }

    public List<Teacher> findTeachersByName(String name) {
        return teacherRepository.findByUser_FullNameContainingIgnoreCase(name);
    }

    public long countStudents() {
        return studentRepository.countStudents();
    }

    public long countTeachers() {
        return teacherRepository.countTeachers();
    }

    public long countAdmins() {
        return adminRepository.countAdmins();
    }

    public long countAllUsers() {
        return userRepository.countUsers();
    }
}