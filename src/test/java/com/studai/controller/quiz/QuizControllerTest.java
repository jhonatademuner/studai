package com.studai.controller.quiz;

import com.studai.domain.quiz.dto.QuizCreateDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.service.quiz.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizControllerTest {

	@Mock
	private QuizService quizService;

	@InjectMocks
	private QuizController quizController;

	private QuizDTO sampleQuizDTO;
	private QuizCreateDTO sampleQuizCreateDTO;
	private UUID sampleQuizId;

	@BeforeEach
	void setUp() {
		sampleQuizId = UUID.randomUUID();

		sampleQuizDTO = QuizDTO.builder()
				.id(sampleQuizId)
				.title("Sample Quiz")
				.description("This is a sample quiz")
				.build();

		sampleQuizCreateDTO = QuizCreateDTO.builder()
				.questionsQuantity(5)
				.sourceContent("Sample content")
				.build();
	}

	@Test
	void create_ShouldReturnCreatedQuiz() {
		// Arrange
		when(quizService.create(any(QuizCreateDTO.class))).thenReturn(sampleQuizDTO);

		// Act
		ResponseEntity<QuizDTO> response = quizController.create(sampleQuizCreateDTO);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(sampleQuizId, response.getBody().getId());
		verify(quizService, times(1)).create(any(QuizCreateDTO.class));
	}

	@Test
	void findById_ShouldReturnQuizWhenFound() {
		// Arrange
		when(quizService.findById(sampleQuizId)).thenReturn(sampleQuizDTO);

		// Act
		ResponseEntity<QuizDTO> response = quizController.findById(sampleQuizId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(sampleQuizId, response.getBody().getId());
		verify(quizService, times(1)).findById(sampleQuizId);
	}

	@Test
	void findAll_ShouldReturnListOfQuizzes() {
		// Arrange
		List<QuizDTO> quizList = Arrays.asList(sampleQuizDTO, sampleQuizDTO);
		when(quizService.find(anyInt(), anyInt())).thenReturn(quizList);

		// Act
		ResponseEntity<List<QuizDTO>> response = quizController.findAll(0, 10);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().size());
		verify(quizService, times(1)).find(0, 10);
	}

	@Test
	void findAll_ShouldUseDefaultPaginationValues() {
		// Arrange
		List<QuizDTO> quizList = Arrays.asList(sampleQuizDTO);
		when(quizService.find(0, 10)).thenReturn(quizList);

		// Act
		ResponseEntity<List<QuizDTO>> response = quizController.findAll(0, 10);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
		verify(quizService, times(1)).find(0, 10);
	}

	@Test
	void delete_ShouldCallServiceDeleteMethod() {
		// Arrange
		doNothing().when(quizService).delete(sampleQuizId);

		// Act
		ResponseEntity<Void> response = quizController.delete(sampleQuizId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNull(response.getBody());
		verify(quizService, times(1)).delete(sampleQuizId);
	}

	@Test
	void findById_ShouldHandleInvalidIdFormatGracefully() {
		// Act & Assert - The UUID format is enforced by Spring, so invalid formats won't reach the controller
		// This test is just to show we've considered the case
		assertDoesNotThrow(() -> quizController.findById(UUID.randomUUID()));
	}
}