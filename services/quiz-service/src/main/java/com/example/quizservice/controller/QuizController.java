package com.example.quizservice.controller;

import com.example.quizservice.client.QuestionClient;
import com.example.quizservice.dto.QuizDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.service.QuizService;
import com.example.quizservice.service.UrlService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@Slf4j
public class QuizController {
    private final QuizService quizService;
    private final UrlService urlService;
    private final QuestionClient questionClient;

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes(HttpServletRequest request) {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        quizzes.forEach(q -> {
            enrichWithCompleteUrl(q, request);
            setQuestionCount(q);
        });
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id, HttpServletRequest request) {
        return quizService.getQuizById(id)
                .map(quiz -> {
                    enrichWithCompleteUrl(quiz, request);
                    setQuestionCount(quiz);
                    return ResponseEntity.ok(quiz);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Quiz>> getLatestQuizzes(HttpServletRequest request) {
        List<Quiz> latestQuizzes = quizService.getLatestQuizzes();
        latestQuizzes.forEach(q -> {
            enrichWithCompleteUrl(q, request);
            setQuestionCount(q);
        });
        return ResponseEntity.ok(latestQuizzes);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Quiz>> getQuizzesByUserId(@PathVariable Long userId, HttpServletRequest request) {
        List<Quiz> quizzes = quizService.getQuizzesByUserId(userId);
        quizzes.forEach(q -> {
            enrichWithCompleteUrl(q, request);
            setQuestionCount(q);
        });
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Quiz>> getPublicQuizzes(HttpServletRequest request) {
        List<Quiz> publicQuizzes = quizService.getPublicQuizzes();
        publicQuizzes.forEach(q -> {
            enrichWithCompleteUrl(q, request);
            setQuestionCount(q);
        });
        return ResponseEntity.ok(publicQuizzes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Quiz>> searchQuizzes(@RequestParam String title, HttpServletRequest request) {
        List<Quiz> searchedQuizzes = quizService.searchQuizzes(title);
        searchedQuizzes.forEach(q -> {
            enrichWithCompleteUrl(q, request);
            setQuestionCount(q);
        });
        return ResponseEntity.ok(searchedQuizzes);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Quiz> createQuiz(@ModelAttribute QuizDTO quizDTO, HttpServletRequest request) {
        try {
            Quiz createdQuiz = quizService.createQuiz(quizDTO, quizDTO.getCoverPhotoFile());
            enrichWithCompleteUrl(createdQuiz, request);
            setQuestionCount(createdQuiz);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Quiz> updateQuiz(
            @PathVariable Long id,
            @ModelAttribute QuizDTO quizDTO,
            HttpServletRequest request) {
        try {
            Quiz updatedQuiz = quizService.updateQuiz(id, quizDTO, quizDTO.getCoverPhotoFile());
            enrichWithCompleteUrl(updatedQuiz, request);
            setQuestionCount(updatedQuiz);
            return ResponseEntity.ok(updatedQuiz);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{id}/increment-question-count")
    public ResponseEntity<Void> incrementQuestionCount(@PathVariable Long id) {
        try {
            quizService.incrementQuestionCount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/decrement-question-count")
    public ResponseEntity<Void> decrementQuestionCount(@PathVariable Long id) {
        try {
            quizService.decrementQuestionCount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/update-question-count/{count}")
    public ResponseEntity<Void> updateQuestionCount(
            @PathVariable("id") Long quizId,
            @PathVariable("count") Integer questionCount) {
        try {
            quizService.updateQuestionCount(quizId, questionCount);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void enrichWithCompleteUrl(Quiz quiz, HttpServletRequest request) {
        if (quiz.getCoverPhoto() != null) {
            quiz.setCoverPhoto(urlService.getCompleteFileUrl(quiz.getCoverPhoto(), request));
        }
    }

    private void setQuestionCount(Quiz quiz) {
        try {
            ResponseEntity<Integer> response = questionClient.getQuestionCountByQuizId(quiz.getId());
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                quiz.setNumberQuestion(response.getBody().intValue());
            } else {
                log.warn("Failed to get question count for quiz ID: {}", quiz.getId());
                quiz.setNumberQuestion(0);
            }
        } catch (Exception e) {
            log.error("Error calling question service for quiz ID: {}", quiz.getId(), e);
            quiz.setNumberQuestion(0);
        }
    }
}