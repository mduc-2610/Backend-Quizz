package com.example.questionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "puzzle_options")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PuzzleOption extends Option {
    private Integer correctPosition;
}