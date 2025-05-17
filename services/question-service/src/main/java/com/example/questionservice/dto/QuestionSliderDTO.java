package com.example.questionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSliderDTO extends QuestionDTO {
    private Integer minValue;
    private Integer maxValue;
    private Integer defaultValue;
    private Integer correctAnswer;
    private String color;
}

