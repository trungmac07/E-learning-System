package com.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.entity.Course;
import com.course.entity.Question;
import com.course.entity.Quiz;
import com.course.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.saveCourse(course));
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{courseId}/quizzes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Quiz> addQuiz(@PathVariable Long courseId, @RequestBody Quiz quiz) {
        Quiz savedQuiz = courseService.addQuizToCourse(courseId, quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    @GetMapping("/{courseId}/quizzes")
    public ResponseEntity<List<Quiz>> getQuizzes(@PathVariable Long courseId) {
        List<Quiz> quizzes = courseService.getQuizzesByCourse(courseId);
        return ResponseEntity.ok(quizzes);
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<Question> addQuestion(@PathVariable Long quizId, @RequestBody Question question) {
        Question savedQuestion = courseService.addQuestionToQuiz(quizId, question);
        return ResponseEntity.ok(savedQuestion);
    }

    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable Long quizId) {
        List<Question> questions = courseService.getQuestionsByQuiz(quizId);
        return ResponseEntity.ok(questions);
}


}
