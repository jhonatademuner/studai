package com.studai.service.quiz;

import com.studai.client.assistant.AssistantClient;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.dto.QuizCreateDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuizQuestionAssembler;
import com.studai.utils.assembler.QuizAssembler;
import com.studai.utils.exception.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserService userService;
    private final AssistantClient assistantClient;
    private final QuizAssembler quizAssembler;

	public QuizService(
            QuizRepository quizRepository,
            QuizAttemptRepository quizAttemptRepository,
            UserService userService,
            AssistantClient assistantClient) {
        this.quizRepository = quizRepository;
        this.userService = userService;
        this.assistantClient = assistantClient;
		QuizQuestionAssembler questionAssembler = new QuizQuestionAssembler(quizRepository);
        this.quizAssembler = new QuizAssembler(userService, questionAssembler);
    }

    public QuizDTO create(QuizCreateDTO quizCreateDTO) {
        QuizDTO quizDTO = this.generateQuiz(quizCreateDTO);
        this.fillQuizAdditionalFields(quizDTO, quizCreateDTO);
        Quiz entity = quizAssembler.toEntity(quizDTO);
        return quizAssembler.toDto(quizRepository.save(entity));
    }

    public QuizDTO findById(UUID id) {
        Quiz quiz = quizRepository.findByIdAndUser(id, userService.getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
        return quizAssembler.toDto(quiz);
    }

    public List<QuizDTO> find(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Quiz> quizzes = quizRepository.findByUser(userService.getCurrentUser(), pageable);
        return quizAssembler.toDtoList(quizzes);
    }

    public void delete(UUID id) {
        Quiz quiz = quizRepository.findByIdAndUser(id, userService.getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
        quizRepository.delete(quiz);
    }

    private QuizDTO generateQuiz(QuizCreateDTO quizCreateDTO) {
        Map<String, String> headers = Map.of("Connection", "keep-alive");
        return assistantClient.postRequest("/quiz", quizCreateDTO, headers, null, QuizDTO.class).getBody();
    }

    private void fillQuizAdditionalFields(QuizDTO quizDTO, QuizCreateDTO quizCreateDTO) {
        quizDTO.setSourceContent(quizCreateDTO.getSourceContent());
        quizDTO.setSourceType(quizCreateDTO.getSourceType());
        quizDTO.setLanguageCode(quizCreateDTO.getLanguageCode());
    }



}
