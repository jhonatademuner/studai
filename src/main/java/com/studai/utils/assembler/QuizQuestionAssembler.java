package com.studai.utils.assembler;

import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.domain.quiz.Quiz;
import com.studai.repository.quiz.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizQuestionAssembler extends AbstractAssembler<QuizQuestion, QuizQuestionDTO> {

    private final QuizRepository quizRepository;

    @Override
    public QuizQuestionDTO toDto(QuizQuestion entity) {
        return QuizQuestionDTO.builder()
                .id(entity.getId())
                .questionType(entity.getQuestionType())
                .statement(entity.getStatement())
                .hint(entity.getHint())
                .explanation(entity.getExplanation())
                .correctAnswer(entity.getCorrectAnswer())
                .quizId(entity.getQuiz().getId())
                .options(entity.getOptions())
                .build();
    }

    @Override
    public QuizQuestion toEntity(QuizQuestionDTO dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + dto.getQuizId()));

        QuizQuestion question = QuizQuestion.builder()
                .id(dto.getId() != null ? dto.getId() : null)
                .questionType(dto.getQuestionType())
                .statement(dto.getStatement())
                .hint(dto.getHint())
                .explanation(dto.getExplanation())
                .correctAnswer(dto.getCorrectAnswer().toString())
                .quiz(quiz)
                .build();

        question.setOptions(dto.getOptions());
        return question;
    }

    @Override
    public QuizQuestion updateEntityFromDto(QuizQuestionDTO dto, QuizQuestion entity) {
        entity.setQuestionType(dto.getQuestionType());
        entity.setStatement(dto.getStatement());
        entity.setHint(dto.getHint());
        entity.setExplanation(dto.getExplanation());
        entity.setCorrectAnswer(dto.getCorrectAnswer().toString());
        entity.setOptions(dto.getOptions());

        if (!entity.getQuiz().getId().equals(dto.getQuizId())) {
            Quiz quiz = quizRepository.findById(dto.getQuizId())
                    .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + dto.getQuizId()));
            entity.setQuiz(quiz);
        }

        return entity;
    }

}