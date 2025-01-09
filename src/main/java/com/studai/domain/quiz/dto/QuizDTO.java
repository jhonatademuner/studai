package com.studai.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studai.domain.question.dto.QuestionDTO;
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

}
