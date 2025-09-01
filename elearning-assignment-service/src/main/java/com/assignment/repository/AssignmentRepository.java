package com.assignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.entity.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByCourseIdAndTeacherId(Long courseId, Long studentId);
    Optional<Assignment> findByCourseIdAndTeacherId(Long courseId, Long studentId);
}