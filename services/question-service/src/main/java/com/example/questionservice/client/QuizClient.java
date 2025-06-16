package com.example.questionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "quiz-service")
public interface QuizClient {

    @GetMapping("/api/quizzes/{id}")
    Object getQuizById(@PathVariable("id") Long id);

    @GetMapping("/api/quizzes/exists/{id}")
    Boolean quizExists(@PathVariable("id") Long id);
    
    @PostMapping("/api/quizzes/{id}/increment-question-count")
    void incrementQuestionCount(@PathVariable("id") Long id);
    
    @PostMapping("/api/quizzes/{id}/decrement-question-count")
    void decrementQuestionCount(@PathVariable("id") Long id);

    @GetMapping("/api/quizzes/{id}/question-count")
    Integer getQuestionCountByQuizId(@PathVariable("id") Long id);
}