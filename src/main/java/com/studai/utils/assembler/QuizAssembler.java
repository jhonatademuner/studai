package com.studai.utils.assembler;

import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.domain.user.User;

import java.util.List;
import java.util.UUID;

public class QuizAssembler {

    public static Quiz toEntity(QuizDTO dto, User user){

        Quiz quiz = Quiz.builder()
            .id(dto.getId() != null ? UUID.fromString(dto.getId()) : null)
            .title(dto.getTitle())
            .description(dto.getDescription())
            .sourceType(dto.getSourceType() != null ? dto.getSourceType() : null)
            .sourceUri(dto.getSourceUri() != null ? dto.getSourceUri() : null)
            .user(user)
            .build();

        List<QuizQuestion> questions = QuizQuestionAssembler.toEntityList(dto.getQuestions(), quiz);
        quiz.setQuestions(questions);

        List<QuizAttempt> attempts = QuizAttemptAssembler.toEntityList(dto.getAttempts(), quiz, user);
        quiz.setAttempts(attempts);

        return quiz;
    }

    public static QuizDTO toDTO(Quiz quiz) {

        List<QuizQuestionDTO> questionDTOs = QuizQuestionAssembler.toDTOList(quiz.getQuestions());
        List<QuizAttemptDTO> attemptDTOS = QuizAttemptAssembler.toDTOList(quiz.getAttempts());

        return QuizDTO.builder()
            .id(quiz.getId() != null ? quiz.getId().toString() : null)
            .title(quiz.getTitle())
            .description(quiz.getDescription())
            .sourceType(quiz.getSourceType())
            .sourceUri(quiz.getSourceUri())
            .userId(quiz.getUser() != null ? quiz.getUser().getId().toString() : null)
            .questions(questionDTOs)
            .attempts(attemptDTOS)
            .build();
    }

}
