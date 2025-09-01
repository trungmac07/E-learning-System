package com.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.AssignmentDto;
import com.assignment.entity.Assignment;
import com.assignment.service.AssignmentService;

@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignTeacher(@RequestBody AssignmentDto assignmentdto)
    {
        try {
            Assignment assignment = assignmentService.assignTeacher(
                assignmentdto.getTeacherId(), 
                assignmentdto.getCourseId()
            );
            return ResponseEntity.ok(assignment);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> unassignTeacher(@RequestBody AssignmentDto assignmentdto)
    {
        try {
            assignmentService.unassignTeacher(
                assignmentdto.getTeacherId(), 
                assignmentdto.getCourseId()
            );
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}