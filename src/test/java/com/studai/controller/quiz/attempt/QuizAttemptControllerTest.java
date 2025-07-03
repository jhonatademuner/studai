package com.studai.controller.quiz.attempt;

import com.studai.domain.quiz.attempt.answer.dto.QuizAttemptAnswerDTO;
import com.studai.domain.quiz.attempt.dto.QuizAttemptCreateDTO;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.service.quiz.attempt.QuizAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizAttemptControllerTest {

	@Mock
	private QuizAttemptService quizAttemptService;

	@InjectMocks
	private QuizAttemptController quizAttemptController;

	private QuizAttemptCreateDTO createDTO;
	private QuizAttemptDTO sampleAttempt;

	@BeforeEach
	void setUp() {
		createDTO = QuizAttemptCreateDTO.builder()
				.quizId(UUID.randomUUID())
				.timeSpent(1500L)
				.guestName("guestname")
				.answers(Map.of(UUID.randomUUID(), "Sample Answer"))
				.build();

		sampleAttempt = QuizAttemptDTO.builder()
				.id(UUID.randomUUID())
				.quizId(createDTO.getQuizId())
				.score(BigDecimal.valueOf(85.0))
				.timeSpent(12345L)
				.guestUser(false)
				.guestName(null)
				.answers(List.of(QuizAttemptAnswerDTO.builder()
					.questionId(UUID.randomUUID())
					.answer("Sample Answer")
					.correct(true)
					.build()))
				.createdAt(LocalDateTime.now())
				.build();
	}

	@Test
	void create_ShouldReturnCreatedAttempt() {
		when(quizAttemptService.create(any(QuizAttemptCreateDTO.class), eq(false))).thenReturn(sampleAttempt);

		ResponseEntity<QuizAttemptDTO> response = quizAttemptController.create(createDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(sampleAttempt.getId(), response.getBody().getId());
		verify(quizAttemptService, times(1)).create(any(QuizAttemptCreateDTO.class), eq(false));
	}

	@Test
	void guestCreate_ShouldReturnCreatedGuestAttempt() {
		when(quizAttemptService.create(any(QuizAttemptCreateDTO.class), eq(true))).thenReturn(sampleAttempt);

		ResponseEntity<QuizAttemptDTO> response = quizAttemptController.guestCreate(createDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(sampleAttempt.getId(), response.getBody().getId());
		verify(quizAttemptService, times(1)).create(any(QuizAttemptCreateDTO.class), eq(true));
	}

	@Test
	void findById_ShouldReturnAttempt() {
		UUID id = sampleAttempt.getId();
		when(quizAttemptService.findById(id)).thenReturn(sampleAttempt);

		ResponseEntity<QuizAttemptDTO> response = quizAttemptController.findById(id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().getId());
		verify(quizAttemptService, times(1)).findById(id);
	}

	@Test
	void findByQuizId_ShouldReturnListOfAttempts() {
		UUID quizId = createDTO.getQuizId();
		List<QuizAttemptDTO> attempts = List.of(sampleAttempt, sampleAttempt);
		when(quizAttemptService.findByQuizId(eq(quizId), anyInt(), anyInt())).thenReturn(attempts);

		ResponseEntity<List<QuizAttemptDTO>> response = quizAttemptController.findByQuizId(quizId, 0, 10);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().size());
		verify(quizAttemptService, times(1)).findByQuizId(eq(quizId), eq(0), eq(10));
	}
}
