package com.studai.domain.quiz.question.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studai.domain.quiz.question.QuizQuestionType;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizQuestionDTO {

    private UUID id;
    private QuizQuestionType questionType;
    private String statement;
    private String hint;
    private String explanation;
    private String correctAnswer;
    private UUID quizId;
    private List<String> options;

}
