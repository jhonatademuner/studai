package com.studai.service.quiz;

import com.studai.client.assistant.AssistantClient;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuizQuestionAssembler;
import com.studai.utils.assembler.QuizAssembler;
import com.studai.utils.assembler.QuizAttemptAssembler;
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
		QuizAttemptAssembler quizAttemptAssembler = new QuizAttemptAssembler(quizRepository, userService);
        this.quizAssembler = new QuizAssembler(userService, questionAssembler, quizAttemptAssembler);
    }

    public QuizDTO generateQuiz(String videoId, int questionsNumber, String language) {
        Map<String, String> headers = Map.of("Connection", "keep-alive");
        Map<String, String> params = Map.of("videoId", videoId, "questionsNumber", String.valueOf(questionsNumber), "language", language);

        return assistantClient.postRequest("/quiz", null, headers, params, QuizDTO.class).getBody();
    }

    public QuizDTO create(String videoId, int questionsNumber, String language){
        QuizDTO quizDTO = generateQuiz(videoId, questionsNumber, language);
        Quiz entity = quizAssembler.toEntity(quizDTO);
        return quizAssembler.toDto(quizRepository.save(entity));
    }

    public QuizDTO findById(UUID id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
        return quizAssembler.toDto(quiz);
    }

    public List<QuizDTO> find(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Quiz> quizzes = quizRepository.findByUser(userService.getCurrentUser(), pageable);
        return quizAssembler.toDtoList(quizzes);
    }

    public void delete(UUID id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
        quizRepository.delete(quiz);
    }

}
