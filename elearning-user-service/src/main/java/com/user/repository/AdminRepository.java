package com.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.user.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    
    Optional<Admin> findByUser_Id(Long userId);
    
    
    @Query("SELECT a FROM Admin a WHERE a.user.keycloakId = :keycloakId")
    Optional<Admin> findByUser_KeycloakId(@Param("keycloakId") String keycloakId);
    
    
    @Query("SELECT a FROM Admin a WHERE a.user.email = :email")
    Optional<Admin> findByUser_Email(@Param("email") String email);
    
    
    @Query("SELECT a FROM Admin a WHERE a.user.username = :username")
    Optional<Admin> findByUser_Username(@Param("username") String username);
    
    
    @Query("SELECT a FROM Admin a WHERE a.user.gender = :gender")
    List<Admin> findByUser_Gender(@Param("gender") Boolean gender);
    
    
    @Query("SELECT a FROM Admin a WHERE LOWER(a.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Admin> findByUser_FullNameContainingIgnoreCase(@Param("name") String name);
    
    
    @Query("SELECT a FROM Admin a JOIN FETCH a.user")
    List<Admin> findAllWithUser();
    
    
    @Query("SELECT a FROM Admin a WHERE a.user.email LIKE CONCAT('%@', :domain)")
    List<Admin> findByUser_EmailDomain(@Param("domain") String domain);
    
    
    boolean existsByUser_Id(Long userId);
    
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Admin a WHERE a.user.keycloakId = :keycloakId")
    boolean existsByUser_KeycloakId(@Param("keycloakId") String keycloakId);
    
    
    @Query("SELECT COUNT(a) FROM Admin a")
    long countAdmins();
}