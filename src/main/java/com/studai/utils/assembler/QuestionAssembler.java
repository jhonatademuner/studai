package com.studai.utils.assembler;

import com.studai.domain.question.Question;
import com.studai.domain.question.dto.QuestionDTO;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.attempt.QuizAttempt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class QuestionAssembler {

    public static Question toEntity(QuestionDTO dto, Quiz quiz) {
        Question entity = Question.builder()
            .id(dto.getId() != null ? UUID.fromString(dto.getId()) : null)
            .questionType(dto.getQuestionType())
            .statement(dto.getStatement())
            .hint(dto.getHint())
            .explanation(dto.getExplanation())
            .correctAnswer(dto.getCorrectAnswer() != null ? dto.getCorrectAnswer().toString() : null)
            .quiz(quiz)
            .build();
        entity.setOptions(dto.getOptions());
        return entity;
    }

    public static QuestionDTO toDTO(Question question) {
        return QuestionDTO.builder()
            .id(question.getId() != null ? question.getId().toString() : null)
            .questionType(question.getQuestionType())
            .statement(question.getStatement())
            .hint(question.getHint())
            .explanation(question.getExplanation())
            .correctAnswer(question.getCorrectAnswer())
            .quizId(question.getQuiz() != null ? question.getQuiz().getId().toString() : null)
            .options(question.getOptions())
            .build();
    }

    public static List<Question> toEntityList(List<QuestionDTO> dtoList, Quiz quiz) {
        if(dtoList == null) return new ArrayList<>();
        return dtoList.stream()
            .map(dto -> toEntity(dto, quiz))
            .collect(Collectors.toList());
    }

    public static List<QuestionDTO> toDTOList(List<Question> questions) {
        if(questions == null) return new ArrayList<>();
        return questions.stream()
            .map(QuestionAssembler::toDTO)
            .collect(Collectors.toList());
    }

}