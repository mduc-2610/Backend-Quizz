package com.example.quizservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private Long userId;
    private Long quizCollectionId;
    private String title;
    private String description;
    private String keyword;
    private String coverPhoto;
    private Boolean visible;
    private Boolean visibleQuizQuestion;
    private Boolean shuffle;
    private Integer numberQuestion;
    
    private transient MultipartFile coverPhotoFile;
}