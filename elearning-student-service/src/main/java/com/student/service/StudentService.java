package com.student.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.entity.Student;
import com.student.repository.StudentRepository;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student syncUserFromKeycloak(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String keycloakId = jwt.getSubject(); 
            String username = jwt.getClaimAsString("preferred_username");
            String email = jwt.getClaimAsString("email");
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");
            
            
            String role = extractPrimaryRole(jwt);
            
            
            Optional<Student> existingStudent = studentRepository.findByKeycloakId(keycloakId);
            
            Student student;
            if (existingStudent.isPresent()) {
                
                student = existingStudent.get();
                student.setUsername(username);
                student.setEmail(email);
                student.setRole(role);
                student.setFullName(buildFullName(firstName, lastName));
            } else {
                
                student = new Student();
                student.setKeycloakId(keycloakId);
                student.setUsername(username);
                student.setEmail(email);
                student.setRole(role);
                student.setFullName(buildFullName(firstName, lastName));
                student.setGender(null); 
            }
            
            return studentRepository.save(student);
        }
        
        throw new RuntimeException("Invalid authentication token");
    }

    public Student getCurrentUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String keycloakId = jwt.getSubject();
            return studentRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found in local database"));
        }
        throw new RuntimeException("Invalid authentication token");
    }


    public Optional<Student> findByKeycloakId(String keycloakId) {
        return studentRepository.findByKeycloakId(keycloakId);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    public Student updateStudent(Long id, Student updatedStudent, Authentication authentication) {
        Student currentUser = getCurrentUser(authentication);
        
        if (!currentUser.getId().equals(id) && !"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("Access denied");
        }
        
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        
        student.setFullName(updatedStudent.getFullName());
        student.setGender(updatedStudent.getGender());
        
        return studentRepository.save(student);
    }

    private String extractPrimaryRole(Jwt jwt) {
        List<String> realmRoles = jwt.getClaimAsStringList("realm_access.roles");
        if (realmRoles == null || realmRoles.isEmpty()) {
            return "student"; 
        }
        
        
        if (realmRoles.contains("admin")) return "admin";
        if (realmRoles.contains("teacher")) return "teacher";
        if (realmRoles.contains("student")) return "student";
        
        return realmRoles.get(0); 
    }

    private String buildFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) return null;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }
}