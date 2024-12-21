package com.studai.domain.question.dto;

import com.studai.domain.question.QuestionType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {

    private QuestionType questionType;
    private String statement;
    private String hint;
    private String explanation;
    private Object correctAnswer;
    private List<String> options;

    public boolean isCorrectAnswer(Object answer) {
        if (answer == null || correctAnswer == null) {
            return false;
        }

        return switch (questionType) {
            case MULTIPLE_CHOICE, TRUE_OR_FALSE ->
                correctAnswer.equals(answer);
            default -> throw new UnsupportedOperationException("QuestionType not supported yet: " + questionType);
        };
    }

}
