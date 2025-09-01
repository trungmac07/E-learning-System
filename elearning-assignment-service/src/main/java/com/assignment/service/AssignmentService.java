package com.assignment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.entity.Assignment;
import com.assignment.repository.AssignmentRepository;

@Service
@Transactional
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment assignTeacher(Long teacherId, Long courseId) {
        
        if(teacherId == null || courseId == null)
        {
            throw new RuntimeException("Invalid authentication token");
        }
        else
        {
            Assignment assignment = new Assignment();
            assignment.setTeacherId(teacherId);
            assignment.setCourseId(courseId);
            assignmentRepository.save(assignment);
            return assignment;
        }

        
    }

    public void unassignTeacher(Long userId, Long courseId) {

        Assignment assignment = assignmentRepository
                .findByCourseIdAndTeacherId(courseId, userId)
                .orElseThrow(() -> new IllegalStateException("Teacher was not assigned for this course!"));

        assignmentRepository.delete(assignment);

    }
}