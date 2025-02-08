package com.studai.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studai.domain.question.dto.QuestionDTO;
import com.studai.domain.quiz.QuizSourceType;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizDTO {

    private String id;
    private String title;
    private String description;
    private List<QuestionDTO> questions;
    private QuizSourceType sourceType;
    private String sourceUri;
    private String userId;
    private List<QuizAttemptDTO> attempts;

}
