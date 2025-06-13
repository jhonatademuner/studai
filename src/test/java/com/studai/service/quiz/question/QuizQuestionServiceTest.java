package com.studai.service.quiz.question;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.question.QuizQuestionRepository;
import com.studai.utils.assembler.QuizQuestionAssembler;
import com.studai.utils.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizQuestionServiceTest {

	private QuizQuestionRepository questionRepository;
	private QuizRepository quizRepository;
	private QuizQuestionService questionService;
	private QuizQuestionAssembler questionAssembler;

	private final UUID questionId = UUID.randomUUID();
	private final UUID quizId = UUID.randomUUID();

	@BeforeEach
	void setUp() {
		questionRepository = mock(QuizQuestionRepository.class);
		quizRepository = mock(QuizRepository.class);
		questionAssembler = mock(QuizQuestionAssembler.class);

		questionService = new QuizQuestionService(questionRepository, questionAssembler, quizRepository);
	}

	@Test
	void testCreate() {
		QuizQuestionDTO dto = new QuizQuestionDTO();
		QuizQuestion entity = new QuizQuestion();
		QuizQuestion saved = new QuizQuestion();
		QuizQuestionDTO resultDto = new QuizQuestionDTO();

		when(questionAssembler.toEntity(dto)).thenReturn(entity);
		when(questionRepository.save(entity)).thenReturn(saved);
		when(questionAssembler.toDto(saved)).thenReturn(resultDto);

		QuizQuestionDTO result = questionService.create(dto);

		assertNotNull(result);
		verify(questionAssembler).toEntity(dto);
		verify(questionRepository).save(entity);
		verify(questionAssembler).toDto(saved);
	}

	@Test
	void testFindById_whenExists() {
		QuizQuestion entity = new QuizQuestion();
		QuizQuestionDTO dto = new QuizQuestionDTO();

		when(questionRepository.findById(questionId)).thenReturn(Optional.of(entity));
		when(questionAssembler.toDto(entity)).thenReturn(dto);

		QuizQuestionDTO result = questionService.findById(questionId);

		assertNotNull(result);
		verify(questionRepository).findById(questionId);
	}

	@Test
	void testFindById_whenNotFound() {
		when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> questionService.findById(questionId));
	}

	@Test
	void testFindByQuizId_whenQuizExists() {
		Quiz quiz = new Quiz();
		List<QuizQuestion> questions = List.of(new QuizQuestion(), new QuizQuestion());
		List<QuizQuestionDTO> questionDTOs = List.of(new QuizQuestionDTO(), new QuizQuestionDTO());
		Pageable pageable = PageRequest.of(0, 10);

		when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
		when(questionRepository.findByQuiz(quiz, pageable)).thenReturn(questions);
		when(questionAssembler.toDtoList(questions)).thenReturn(questionDTOs);

		List<QuizQuestionDTO> result = questionService.findByQuizId(quizId, 0, 10);

		assertEquals(2, result.size());
	}

	@Test
	void testFindByQuizId_whenQuizNotFound() {
		when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> questionService.findByQuizId(quizId, 0, 10));
	}

	@Test
	void testUpdate_whenExists() {
		QuizQuestionDTO dto = new QuizQuestionDTO();
		dto.setId(questionId);

		QuizQuestion existing = new QuizQuestion();
		QuizQuestion updated = new QuizQuestion();
		QuizQuestion saved = new QuizQuestion();
		QuizQuestionDTO resultDto = new QuizQuestionDTO();

		when(questionRepository.findById(questionId)).thenReturn(Optional.of(existing));
		when(questionAssembler.updateEntityFromDto(dto, existing)).thenReturn(updated);
		when(questionRepository.save(updated)).thenReturn(saved);
		when(questionAssembler.toDto(saved)).thenReturn(resultDto);

		QuizQuestionDTO result = questionService.update(dto);

		assertNotNull(result);
		verify(questionRepository).findById(questionId);
		verify(questionAssembler).updateEntityFromDto(dto, existing);
		verify(questionRepository).save(updated);
	}

	@Test
	void testUpdate_whenNotFound() {
		QuizQuestionDTO dto = new QuizQuestionDTO();
		dto.setId(questionId);

		when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> questionService.update(dto));
	}

	@Test
	void testDelete_whenExists() {
		QuizQuestion entity = new QuizQuestion();
		when(questionRepository.findById(questionId)).thenReturn(Optional.of(entity));

		questionService.delete(questionId);

		verify(questionRepository).delete(entity);
	}

	@Test
	void testDelete_whenNotFound() {
		when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> questionService.delete(questionId));
	}
}
