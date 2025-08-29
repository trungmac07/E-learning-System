package com.student.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // local DB PK

    @Column(nullable = false, unique = true)
    private String keycloakId; // maps to Keycloak user UUID
    private String username;   // sync with Keycloak
    private String email;
    private String role;       // optional, but usually taken from Keycloak
    private String fullName;
    private Boolean gender; //0 male - 1 female
}
