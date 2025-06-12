package com.studai.utils.assembler;

import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.user.User;
import com.studai.repository.quiz.QuizRepository;
import com.studai.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizAttemptAssembler extends AbstractAssembler<QuizAttempt, QuizAttemptDTO> {

    private final QuizRepository quizRepository;
    private final UserService userService;
    private final QuizAttemptAnswerAssembler attemptAnswerAssembler;

    @Override
    public QuizAttemptDTO toDto(QuizAttempt quizAttempt) {
        return QuizAttemptDTO.builder()
            .id(quizAttempt.getId() != null ? quizAttempt.getId() : null)
            .quizId(quizAttempt.getQuiz() != null ? quizAttempt.getQuiz().getId() : null)
            .score(quizAttempt.getScore())
            .answers(attemptAnswerAssembler.toDtoList(quizAttempt.getAnswers()))
            .guestUser(quizAttempt.isGuestUser())
            .guestName(quizAttempt.isGuestUser() ? quizAttempt.getGuestName() : null)
            .createdAt(quizAttempt.getCreatedAt())
            .timeSpent(quizAttempt.getTimeSpent())
            .build();
    }

    @Override
    public QuizAttempt toEntity(QuizAttemptDTO dto) {
        Quiz quiz = dto.getQuizId() == null ? null : quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + dto.getQuizId()));

        User user;
        if(dto.isGuestUser() && quiz != null) {
            user = quiz.getUser();
        } else {
            user = userService.getCurrentUser();
        }

        QuizAttempt attempt = QuizAttempt.builder()
                .id(dto.getId() != null ? dto.getId() : null)
                .quiz(quiz)
                .user(user)
                .score(dto.getScore())
                .guestUser(dto.isGuestUser())
                .guestName(dto.isGuestUser() ? dto.getGuestName() : null)
                .createdAt(dto.getCreatedAt())
                .timeSpent(dto.getTimeSpent())
                .build();

        attemptAnswerAssembler.toEntityList(dto.getAnswers())
                .forEach(attempt::addAnswer);

        return attempt;
    }

}
