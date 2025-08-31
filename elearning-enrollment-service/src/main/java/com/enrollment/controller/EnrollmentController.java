package com.enrollment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrollment.dto.EnrollmentDto;
import com.enrollment.entity.Enrollment;
import com.enrollment.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> enrollStudent(@RequestBody EnrollmentDto enrollmentdto, Authentication authentication)
    {
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(
                authentication, 
                enrollmentdto.getCourseId()
            );
            return ResponseEntity.ok(enrollment);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> unenrollStudent(@RequestBody EnrollmentDto enrollmentdto)
    {
        try {
            enrollmentService.unenrollStudent(
                enrollmentdto.getStudentId(), 
                enrollmentdto.getCourseId()
            );
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}