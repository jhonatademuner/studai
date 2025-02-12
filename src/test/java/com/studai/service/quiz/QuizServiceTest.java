package com.studai.service.quiz;

import com.studai.client.assistant.AssistantClient;
import com.studai.domain.question.Question;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.QuizSourceType;
import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.domain.user.User;
import com.studai.repository.question.QuestionRepository;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.service.user.UserService;
import com.studai.utils.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @InjectMocks
    private QuizService quizService;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserService userService;

    @Mock
    private AssistantClient assistantClient;

    private User user;
    private Quiz quiz;
    private QuizDTO quizDTO;
    private UUID quizId;

    @BeforeEach
    void setUp() {
        quizId = UUID.randomUUID();
        user = new User();
        user.setId(UUID.randomUUID());
        quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setUser(user);
        quiz.setSourceType(QuizSourceType.YOUTUBE_VIDEO);
        quizDTO = new QuizDTO();
        quizDTO.setId(quizId.toString());
    }

    @Test
    void testFindById_Success() {
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        QuizDTO result = quizService.findById(quizId.toString());

        assertNotNull(result);
        assertEquals(quizId.toString(), result.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> quizService.findById(quizId.toString()));
    }

    @Test
    void testFindAll_Success() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(quizRepository.findByUser(user)).thenReturn(List.of(quiz));

        List<QuizDTO> results = quizService.findAll();

        assertFalse(results.isEmpty());
        assertEquals(quizId.toString(), results.get(0).getId());
    }

    @Test
    void testFindAll_Empty() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(quizRepository.findByUser(user)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> quizService.findAll());
    }

    @Test
    void testDelete_Success() {
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        doNothing().when(quizRepository).delete(quiz);

        QuizDTO result = quizService.delete(quizId.toString());

        assertNotNull(result);
        assertEquals(quizId.toString(), result.getId());
        verify(quizRepository, times(1)).delete(quiz);
    }

    @Test
    void testDelete_NotFound() {
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> quizService.delete(quizId.toString()));
    }

    @Test
    void testSubmitAttempt_Success() {
        QuizAttempt attempt = QuizAttempt.builder()
                .quiz(quiz)
                .user(user)
                .score(80.0)
                .timeSpent(600L)
                .completionDate(LocalDateTime.now())
                .build();

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(userService.getCurrentUser()).thenReturn(user);
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenReturn(attempt);

        QuizAttemptDTO result = quizService.submitAttempt(quizId.toString(), 80.0, 600L);

        assertNotNull(result);
        verify(quizAttemptRepository, times(1)).save(any(QuizAttempt.class));
    }
}
