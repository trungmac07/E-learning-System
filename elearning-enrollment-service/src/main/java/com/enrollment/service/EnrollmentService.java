package com.enrollment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enrollment.entity.Enrollment;
import com.enrollment.grpc.UserIdClient;
import com.enrollment.repository.EnrollmentRepository;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserIdClient userIdClient;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment enrollStudent(Authentication authentication, Long courseId) {
        
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String keycloakId = jwt.getSubject();
            Long userId = userIdClient.getInternalId(keycloakId);

            if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, userId)) {
                throw new IllegalStateException("Student already enrolled in this course!");
            }
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(userId);
            enrollment.setCourseId(courseId);
    
            enrollmentRepository.save(enrollment);
            return enrollment;
        }

        throw new RuntimeException("Invalid authentication token");
    }

    public void unenrollStudent(Long userId, Long courseId) {

        Enrollment enrollment = enrollmentRepository
                .findByCourseIdAndStudentId(courseId, userId)
                .orElseThrow(() -> new IllegalStateException("Student did not enroll for this course!"));

        enrollmentRepository.delete(enrollment);

    }
}