package com.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.course.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
