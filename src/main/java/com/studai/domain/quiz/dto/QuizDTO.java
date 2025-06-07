package com.studai.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.domain.quiz.QuizSourceType;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizDTO {

    private UUID id;
    private String title;
    private String description;
    private List<QuizQuestionDTO> questions;
    private QuizSourceType sourceType;
    private String sourceUri;
    private String userId;
    private List<QuizAttemptDTO> attempts = new ArrayList<>();

}
