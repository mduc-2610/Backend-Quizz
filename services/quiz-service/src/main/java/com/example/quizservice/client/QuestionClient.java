package com.example.quizservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "question-service")
public interface QuestionClient {
    
    @GetMapping("/api/questions/quiz/{quizId}/count")
    ResponseEntity<Integer> getQuestionCountByQuizId(@PathVariable("quizId") Long quizId);
}