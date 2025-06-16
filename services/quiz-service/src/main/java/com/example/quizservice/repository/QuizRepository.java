package com.example.quizservice.repository;


import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByUserId(Long userId);
    List<Quiz> findByVisibleTrue();
    List<Quiz> findByTitleContainingIgnoreCase(String title);
    List<Quiz> findTop10ByOrderByCreatedAtDesc();

    
    Set<Quiz> findByQuizCollection(QuizCollection quizCollection);
    Set<Quiz> findByQuizCollectionId(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE quizzes SET collection_id = NULL WHERE collection_id = :collectionId", 
           nativeQuery = true)
    int removeCollectionReference(@Param("collectionId") Long collectionId);
}