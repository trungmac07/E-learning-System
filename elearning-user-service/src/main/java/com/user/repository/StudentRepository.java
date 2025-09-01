package com.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.user.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    
    Optional<Student> findByUser_Id(Long userId);
    
    
    @Query("SELECT s FROM Student s WHERE s.user.keycloakId = :keycloakId")
    Optional<Student> findByUser_KeycloakId(@Param("keycloakId") String keycloakId);
    
    
    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Optional<Student> findByUser_Email(@Param("email") String email);
    
    
    @Query("SELECT s FROM Student s WHERE s.user.username = :username")
    Optional<Student> findByUser_Username(@Param("username") String username);
    
    
    @Query("SELECT s FROM Student s WHERE s.user.gender = :gender")
    List<Student> findByUser_Gender(@Param("gender") Boolean gender);
    
    
    @Query("SELECT s FROM Student s WHERE LOWER(s.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByUser_FullNameContainingIgnoreCase(@Param("name") String name);
    
    @Query("SELECT s FROM Student s JOIN FETCH s.user")
    List<Student> findAllWithUser();
    
    
    @Query("SELECT s FROM Student s WHERE s.user.email LIKE CONCAT('%@', :domain)")
    List<Student> findByUser_EmailDomain(@Param("domain") String domain);
    
    
    boolean existsByUser_Id(Long userId);
    
    
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.user.keycloakId = :keycloakId")
    boolean existsByUser_KeycloakId(@Param("keycloakId") String keycloakId);
    
    
    @Query("SELECT COUNT(s) FROM Student s")
    long countStudents();
}