package com.studai.service.quiz.attempt;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.dto.QuizAttemptCreateDTO;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuizAttemptAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuizAttemptServiceTest {

	private QuizAttemptRepository quizAttemptRepository;
	private QuizAttemptAssembler quizAttemptAssembler;
	private QuizRepository quizRepository;
	private UserService userService;

	private QuizAttemptService quizAttemptService;

	@BeforeEach
	void setUp() {
		quizAttemptRepository = mock(QuizAttemptRepository.class);
		quizAttemptAssembler = mock(QuizAttemptAssembler.class);
		quizRepository = mock(QuizRepository.class);
		userService = mock(UserService.class);

		quizAttemptService = new QuizAttemptService(
				quizAttemptRepository,
				quizAttemptAssembler,
				quizRepository,
				userService
		);
	}

	@Test
	void testCreateAttempt_WithCorrectScoring() {
		UUID quizId = UUID.randomUUID();
		UUID questionId = UUID.randomUUID();
		String correctAnswer = "42";

		QuizAttemptCreateDTO createDTO = QuizAttemptCreateDTO.builder()
				.quizId(quizId)
				.answers(Map.of(questionId, correctAnswer))
				.timeSpent(1000L)
				.guestName("Guest")
				.build();

		QuizQuestion question = QuizQuestion.builder()
				.id(questionId)
				.correctAnswer(correctAnswer)
				.build();

		Quiz quiz = Quiz.builder().id(quizId).questions(List.of(question)).build();

		when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

		QuizAttempt entity = new QuizAttempt(); // can be empty for this test
		QuizAttemptDTO expectedDTO = QuizAttemptDTO.builder()
				.quizId(quizId)
				.score(BigDecimal.valueOf(100.00))
				.guestUser(true)
				.guestName("Guest")
				.build();

		when(quizAttemptAssembler.toEntity(any())).thenReturn(entity);
		when(quizAttemptRepository.save(entity)).thenReturn(entity);
		when(quizAttemptAssembler.toDto(entity)).thenReturn(expectedDTO);

		QuizAttemptDTO result = quizAttemptService.create(createDTO, true);

		assertNotNull(result);
		assertEquals(BigDecimal.valueOf(100.00), result.getScore());
		assertEquals("Guest", result.getGuestName());
		assertTrue(result.isGuestUser());
	}

	@Test
	void testFindById_Success() {
		UUID attemptId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		QuizAttempt entity = new QuizAttempt();
		QuizAttemptDTO dto = QuizAttemptDTO.builder().quizId(UUID.randomUUID()).build();

		var user = mock(com.studai.domain.user.User.class);
		when(user.getId()).thenReturn(userId);
		when(userService.getCurrentUser()).thenReturn(user);
		when(quizAttemptRepository.findByUserIdAndId(userId, attemptId)).thenReturn(Optional.of(entity));
		when(quizAttemptAssembler.toDto(entity)).thenReturn(dto);

		QuizAttemptDTO result = quizAttemptService.findById(attemptId);

		assertNotNull(result);
		verify(quizAttemptRepository).findByUserIdAndId(userId, attemptId);
	}

	@Test
	void testFindById_NotFound_ShouldThrow() {
		UUID attemptId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		var user = mock(com.studai.domain.user.User.class);
		when(user.getId()).thenReturn(userId);
		when(userService.getCurrentUser()).thenReturn(user);
		when(quizAttemptRepository.findByUserIdAndId(userId, attemptId)).thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> quizAttemptService.findById(attemptId));

		assertEquals("Quiz attempt not found with ID: " + attemptId, ex.getMessage());
	}

	@Test
	void testFindByQuizId_ShouldReturnResults() {
		UUID quizId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		Quiz quiz = Quiz.builder().id(quizId).questions(List.of()).build();
		List<QuizAttempt> attempts = List.of(new QuizAttempt());

		var user = mock(com.studai.domain.user.User.class);
		when(user.getId()).thenReturn(userId);
		when(userService.getCurrentUser()).thenReturn(user);
		when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
		when(quizAttemptRepository.findByUserIdAndQuizId(eq(userId), eq(quizId), any())).thenReturn(attempts);
		when(quizAttemptAssembler.toDtoList(attempts)).thenReturn(List.of(QuizAttemptDTO.builder().quizId(quizId).build()));

		List<QuizAttemptDTO> result = quizAttemptService.findByQuizId(quizId, 0, 10);

		assertEquals(1, result.size());
		assertEquals(quizId, result.get(0).getQuizId());
	}

	@Test
	void testFindByQuizId_QuizNotFound_ShouldThrow() {
		UUID quizId = UUID.randomUUID();
		when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> quizAttemptService.findByQuizId(quizId, 0, 10));

		assertEquals("Quiz not found with ID: " + quizId, ex.getMessage());
	}
}
