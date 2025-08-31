package com.enrollment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enrollment.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);
}