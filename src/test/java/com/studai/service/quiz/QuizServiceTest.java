package com.studai.service.quiz;

import com.studai.client.assistant.AssistantClient;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.QuizLanguage;
import com.studai.domain.quiz.QuizSourceType;
import com.studai.domain.quiz.dto.QuizCreateDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.domain.user.User;
import com.studai.repository.quiz.QuizRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuizAssembler;
import com.studai.utils.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    private QuizService quizService;
    private QuizRepository quizRepository;
    private QuizAssembler quizAssembler;
    private UserService userService;
    private AssistantClient assistantClient;

    private final UUID quizId = UUID.randomUUID();
    private final User currentUser = User.builder()
			.id(UUID.randomUUID())
			.username("testUser")
			.email("testUser@example.com")
			.build();;

    @BeforeEach
    void setUp() {
        quizRepository = mock(QuizRepository.class);
        quizAssembler = mock(QuizAssembler.class);
        userService = mock(UserService.class);
        assistantClient = mock(AssistantClient.class);

        quizService = new QuizService(quizRepository, quizAssembler, userService, assistantClient);

        when(userService.getCurrentUser()).thenReturn(currentUser);
    }

    @Test
    void testCreateQuiz() {
        QuizCreateDTO createDTO = new QuizCreateDTO();
        createDTO.setSourceContent("some content");
        createDTO.setSourceType(QuizSourceType.PROMPT_BASED);
        createDTO.setLanguageCode(QuizLanguage.PT);

        QuizDTO generatedDTO = new QuizDTO();
        generatedDTO.setId(quizId);
        generatedDTO.setTitle("Generated Quiz");

        Quiz entity = new Quiz();
        entity.setId(quizId);

        when(assistantClient.postRequest(anyString(), any(), isNull(), isNull(), eq(QuizDTO.class)))
                .thenReturn(ResponseEntity.ok(generatedDTO));
        when(quizAssembler.toEntity(generatedDTO)).thenReturn(entity);
        when(quizRepository.save(entity)).thenReturn(entity);
        when(quizAssembler.toDto(entity)).thenReturn(generatedDTO);

        QuizDTO result = quizService.create(createDTO);

        assertNotNull(result);
        assertEquals("Generated Quiz", result.getTitle());
        verify(quizRepository).save(entity);
    }

    @Test
    void testFindById_WhenExists() {
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quizId);

        when(quizRepository.findByIdAndUser(quizId, currentUser)).thenReturn(Optional.of(quiz));
        when(quizAssembler.toDto(quiz)).thenReturn(quizDTO);

        QuizDTO result = quizService.findById(quizId);

        assertNotNull(result);
        assertEquals(quizId, result.getId());
    }

    @Test
    void testFindById_WhenNotFound() {
        when(quizRepository.findByIdAndUser(quizId, currentUser)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> quizService.findById(quizId));
    }

    @Test
    void testFindAllPaginated() {
        Quiz quiz1 = new Quiz();
        Quiz quiz2 = new Quiz();
        List<Quiz> quizzes = List.of(quiz1, quiz2);

        QuizDTO dto1 = new QuizDTO();
        QuizDTO dto2 = new QuizDTO();
        List<QuizDTO> quizDTOs = List.of(dto1, dto2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        when(quizRepository.findByUser(currentUser, pageable)).thenReturn(quizzes);
        when(quizAssembler.toDtoList(quizzes)).thenReturn(quizDTOs);

        List<QuizDTO> result = quizService.find(0, 10);

        assertEquals(2, result.size());
    }

    @Test
    void testDelete_WhenExists() {
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        when(quizRepository.findByIdAndUser(quizId, currentUser)).thenReturn(Optional.of(quiz));

        quizService.delete(quizId);

        verify(quizRepository).delete(quiz);
    }

    @Test
    void testDelete_WhenNotFound() {
        when(quizRepository.findByIdAndUser(quizId, currentUser)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> quizService.delete(quizId));
    }
}
