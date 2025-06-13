package com.studai.controller.quiz.question;

import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.domain.quiz.question.QuizQuestionType;
import com.studai.service.quiz.question.QuizQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizQuestionControllerTest {

	@Mock
	private QuizQuestionService questionService;

	@InjectMocks
	private QuizQuestionController quizQuestionController;

	private QuizQuestionDTO sampleQuestion;
	private UUID sampleQuestionId;
	private UUID sampleQuizId;

	@BeforeEach
	void setUp() {
		sampleQuestionId = UUID.randomUUID();
		sampleQuizId = UUID.randomUUID();

		sampleQuestion = QuizQuestionDTO.builder()
				.id(sampleQuestionId)
				.questionType(QuizQuestionType.MULTIPLE_CHOICE)
				.statement("What is the capital of France?")
				.hint("It's a famous European city.")
				.explanation("Paris is the capital and most populous city of France.")
				.correctAnswer("Paris")
				.quizId(sampleQuizId)
				.options(List.of("Paris", "Berlin", "Madrid", "Rome"))
				.build();
	}

	@Test
	void create_ShouldReturnCreatedQuestion() {
		when(questionService.create(any(QuizQuestionDTO.class))).thenReturn(sampleQuestion);

		ResponseEntity<QuizQuestionDTO> response = quizQuestionController.create(sampleQuestion);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(sampleQuestionId, response.getBody().getId());
		verify(questionService, times(1)).create(any(QuizQuestionDTO.class));
	}

	@Test
	void findById_ShouldReturnQuestionWhenFound() {
		when(questionService.findById(sampleQuestionId)).thenReturn(sampleQuestion);

		ResponseEntity<QuizQuestionDTO> response = quizQuestionController.findById(sampleQuestionId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(sampleQuestionId, response.getBody().getId());
		verify(questionService, times(1)).findById(sampleQuestionId);
	}

	@Test
	void findByQuizId_ShouldReturnQuestionsForGivenQuiz() {
		List<QuizQuestionDTO> questionList = List.of(sampleQuestion);
		when(questionService.findByQuizId(sampleQuizId, 0, 10)).thenReturn(questionList);

		ResponseEntity<List<QuizQuestionDTO>> response = quizQuestionController.findByQuizId(sampleQuizId, 0, 10);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
		assertEquals(sampleQuestionId, response.getBody().get(0).getId());
		verify(questionService, times(1)).findByQuizId(sampleQuizId, 0, 10);
	}

	@Test
	void update_ShouldReturnUpdatedQuestion() {
		when(questionService.update(any(QuizQuestionDTO.class))).thenReturn(sampleQuestion);

		ResponseEntity<QuizQuestionDTO> response = quizQuestionController.update(sampleQuestion);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(sampleQuestionId, response.getBody().getId());
		verify(questionService, times(1)).update(any(QuizQuestionDTO.class));
	}

	@Test
	void delete_ShouldCallServiceDeleteMethod() {
		doNothing().when(questionService).delete(sampleQuestionId);

		ResponseEntity<Void> response = quizQuestionController.delete(sampleQuestionId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNull(response.getBody());
		verify(questionService, times(1)).delete(sampleQuestionId);
	}

	@Test
	void findById_ShouldHandleValidUUID() {
		assertDoesNotThrow(() -> quizQuestionController.findById(UUID.randomUUID()));
	}
}
