package com.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.user.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    
    Optional<Teacher> findByUser_Id(Long userId);
    
    
    @Query("SELECT t FROM Teacher t WHERE t.user.keycloakId = :keycloakId")
    Optional<Teacher> findByUser_KeycloakId(@Param("keycloakId") String keycloakId);
    
    
    @Query("SELECT t FROM Teacher t WHERE t.user.email = :email")
    Optional<Teacher> findByUser_Email(@Param("email") String email);
    
    
    @Query("SELECT t FROM Teacher t WHERE t.user.username = :username")
    Optional<Teacher> findByUser_Username(@Param("username") String username);
    
    
    List<Teacher> findByDepartment(String department);
    
    
    List<Teacher> findByDepartmentIgnoreCase(String department);
    
    
    @Query("SELECT t FROM Teacher t WHERE t.user.gender = :gender")
    List<Teacher> findByUser_Gender(@Param("gender") Boolean gender);
    
    
    @Query("SELECT t FROM Teacher t WHERE t.department = :department AND t.user.gender = :gender")
    List<Teacher> findByDepartmentAndUser_Gender(@Param("department") String department, @Param("gender") Boolean gender);
    
    
    @Query("SELECT t FROM Teacher t WHERE LOWER(t.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Teacher> findByUser_FullNameContainingIgnoreCase(@Param("name") String name);
    
    
    @Query("SELECT t FROM Teacher t JOIN FETCH t.user")
    List<Teacher> findAllWithUser();
    
    
    @Query("SELECT t FROM Teacher t WHERE t.user.email LIKE CONCAT('%@', :domain)")
    List<Teacher> findByUser_EmailDomain(@Param("domain") String domain);
    
    
    @Query("SELECT DISTINCT t.department FROM Teacher t WHERE t.department IS NOT NULL ORDER BY t.department")
    List<String> findDistinctDepartments();
    
    
    long countByDepartment(String department);
    
    
    boolean existsByUser_Id(Long userId);
    
    
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Teacher t WHERE t.user.keycloakId = :keycloakId")
    boolean existsByUser_KeycloakId(@Param("keycloakId") String keycloakId);

    @Query("SELECT COUNT(t) FROM Teacher t")
    long countTeachers();
}