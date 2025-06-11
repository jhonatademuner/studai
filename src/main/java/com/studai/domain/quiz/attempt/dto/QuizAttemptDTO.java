package com.studai.domain.quiz.attempt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studai.domain.quiz.attempt.answer.dto.QuizAttemptAnswerDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    private boolean guestUser;
    private String guestName;
    private List<QuizAttemptAnswerDTO> answers;
    private LocalDateTime createdAt;

}
