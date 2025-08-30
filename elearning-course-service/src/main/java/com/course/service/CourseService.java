package com.course.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.entity.Course;
import com.course.entity.Question;
import com.course.entity.Quiz;
import com.course.repository.CourseRepository;
import com.course.repository.QuestionRepository;
import com.course.repository.QuizRepository;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    
    
    public CourseService(CourseRepository courseRepository, QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.courseRepository = courseRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }
    
  
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    
    public Quiz addQuizToCourse(Long courseId, Quiz quiz) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }

        Course course = optionalCourse.get();
        quiz.setCourse(course);  
        return quizRepository.save(quiz);
    }

    
    public List<Quiz> getQuizzesByCourse(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }

        return optionalCourse.get().getQuizzes();
    }
    
    
    public Question addQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + quizId));
        question.setQuiz(quiz);
        return questionRepository.save(question);
    }
    
    
    public List<Question> getQuestionsByQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + quizId));
        return quiz.getQuestions();
    }
    
}
