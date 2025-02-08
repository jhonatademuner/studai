package com.studai.domain.quiz.attempt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizAttemptDTO {

    private String id;
    private String quizId;
    private String userId;
    private Double score;
    private LocalDateTime completionDate;
    private Long timeSpent;

}
