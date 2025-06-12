package com.studai.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studai.domain.quiz.QuizLanguage;
import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.domain.quiz.QuizSourceType;
import lombok.*;

import java.time.LocalDateTime;
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
    private QuizLanguage languageCode;
    private QuizSourceType sourceType;
    private String sourceContent;
    private List<QuizQuestionDTO> questions = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
