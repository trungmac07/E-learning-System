package com.student.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByKeycloakId(String keycloakId);
    Optional<Student> findByUsername(String username);
    List<Student> findByRole(String role);
    boolean existsByKeycloakId(String keycloakId);
    Optional<Student> findByEmail(String email);

}