package com.example.quizservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "quizzes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    private String description;

    private String keyword;

    private Integer score;

    private String coverPhoto;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer numberQuestion = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean visible;

    private Boolean visibleQuizQuestion;

    private Boolean shuffle;
    
    @ManyToOne
    @JoinColumn(name = "collection_id")
    @JsonIgnore
    private QuizCollection quizCollection;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private Set<QuizGame> quizGames;

    @JsonProperty("quizCollectionId")
    public Long getCollectionId() {
        return quizCollection != null ? quizCollection.getId() : null;
    }
}