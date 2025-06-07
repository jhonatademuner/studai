package com.studai.domain.quiz.attempt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizAttemptDTO {

    private UUID id;
    private UUID quizId;
    private BigDecimal score;
    private Long timeSpent;
    private LocalDateTime createdAt;

}
