package com.studai.service.question;

import com.studai.domain.question.Question;
import com.studai.domain.question.dto.QuestionDTO;
import com.studai.domain.quiz.Quiz;
import com.studai.repository.question.QuestionRepository;
import com.studai.repository.quiz.QuizRepository;
import com.studai.utils.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizRepository quizRepository;

    private Question question;
    private QuestionDTO questionDTO;
    private UUID questionId;
    private UUID quizId;
    private Quiz quiz;

    @BeforeEach
    void setUp() {
        questionId = UUID.randomUUID();
        quizId = UUID.randomUUID();
        quiz = new Quiz();
        quiz.setId(quizId);

        question = new Question();
        question.setId(questionId);
        question.setQuiz(quiz);

        questionDTO = new QuestionDTO();
        questionDTO.setId(questionId.toString());
        questionDTO.setQuizId(quizId.toString());
    }

    @Test
    void testCreate_Success() {
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        QuestionDTO result = questionService.create(questionDTO);

        assertNotNull(result);
        assertEquals(questionId.toString(), result.getId());
    }

    @Test
    void testCreate_QuizNotFound() {
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.create(questionDTO));
    }

    @Test
    void testFindById_Success() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        QuestionDTO result = questionService.findById(questionId.toString());

        assertNotNull(result);
        assertEquals(questionId.toString(), result.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.findById(questionId.toString()));
    }

    @Test
    void testUpdate_Success() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        QuestionDTO result = questionService.update(questionDTO);

        assertNotNull(result);
        assertEquals(questionId.toString(), result.getId());
    }

    @Test
    void testUpdate_NotFound() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.update(questionDTO));
    }

    @Test
    void testDelete_Success() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        doNothing().when(questionRepository).delete(question);

        QuestionDTO result = questionService.delete(questionId.toString());

        assertNotNull(result);
        assertEquals(questionId.toString(), result.getId());
        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void testDelete_NotFound() {
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.delete(questionId.toString()));
    }
}
