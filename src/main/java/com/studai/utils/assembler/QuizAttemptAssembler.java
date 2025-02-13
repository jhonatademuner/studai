package com.studai.utils.assembler;

import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class QuizAttemptAssembler {

    public static QuizAttempt toEntity(QuizAttemptDTO dto, Quiz quiz, User user) {
        return QuizAttempt.builder()
            .id(dto.getId() != null ? UUID.fromString(dto.getId()) : null)
            .quiz(quiz)
            .user(user)
            .score(dto.getScore())
            .completionDate(dto.getCompletionDate())
            .timeSpent(dto.getTimeSpent())
            .build();
    }

    public static QuizAttemptDTO toDTO(QuizAttempt quizAttempt) {
        return QuizAttemptDTO.builder()
            .id(quizAttempt.getId() != null ? quizAttempt.getId().toString() : null)
            .quizId(quizAttempt.getQuiz() != null ? quizAttempt.getQuiz().getId().toString() : null)
            .userId(quizAttempt.getUser() != null ? quizAttempt.getUser().getId().toString() : null)
            .score(quizAttempt.getScore())
            .completionDate(quizAttempt.getCompletionDate())
            .timeSpent(quizAttempt.getTimeSpent())
            .build();
    }

    public static List<QuizAttempt> toEntityList(List<QuizAttemptDTO> dtoList, Quiz quiz, User user) {
        if(dtoList == null || dtoList.isEmpty()) return new ArrayList<>();
        return dtoList.stream()
            .map(dto -> toEntity(dto, quiz, user))
            .collect(Collectors.toList());
    }

    public static List<QuizAttemptDTO> toDTOList(List<QuizAttempt> questions) {
        if(questions == null || questions.isEmpty()) return new ArrayList<>();
        return questions.stream()
            .map(QuizAttemptAssembler::toDTO)
            .collect(Collectors.toList());
    }

}
