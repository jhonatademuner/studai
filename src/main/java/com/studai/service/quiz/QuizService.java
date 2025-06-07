package com.studai.service.quiz;

import com.studai.client.assistant.AssistantClient;
import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.QuizSourceType;
import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.repository.quiz.question.QuizQuestionRepository;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuizQuestionAssembler;
import com.studai.utils.assembler.QuizAssembler;
import com.studai.utils.assembler.QuizAttemptAssembler;
import com.studai.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private QuizQuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AssistantClient assistantClient;

    public QuizDTO generateQuiz(String videoId, int questionsNumber, String language) {
        Map<String, String> headers = Map.of("Connection", "keep-alive");
        Map<String, String> params = Map.of("videoId", videoId, "questionsNumber", String.valueOf(questionsNumber), "language", language);

        return assistantClient.postRequest("/quiz", null, headers, params, QuizDTO.class).getBody();
    }

    public QuizDTO create(String videoId, int questionsNumber, String language){
        QuizDTO quizDTO = generateQuiz(videoId, questionsNumber, language);
        Quiz entity = QuizAssembler.toEntity(quizDTO, userService.getCurrentUser());
        entity.setSourceType(QuizSourceType.YOUTUBE_VIDEO);
        entity.setSourceUri(videoId);
        quizRepository.save(entity);
        return QuizAssembler.toDTO(entity);
    }

    public QuizDTO findById(String id) {
        Quiz quiz = quizRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
        return QuizAssembler.toDTO(quiz);
    }

    public List<QuizDTO> find(int page, int size) {
        List<Quiz> quizzes = quizRepository.findByUser(userService.getCurrentUser());

        List<QuizDTO> quizDtos = new ArrayList<>();
        for(Quiz quiz : quizzes){
            quizDtos.add(QuizAssembler.toDTO(quiz));
        }
        return quizDtos;
    }

    public QuizDTO update(QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(UUID.fromString(quizDTO.getId()))
            .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + quizDTO.getId()));

        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());

        List<QuizQuestion> updatedQuestions = QuizQuestionAssembler.toEntityList(quizDTO.getQuestions(), quiz);
        List<QuizQuestion> existingQuestions = quiz.getQuestions();

        existingQuestions.removeIf(existing ->
            updatedQuestions.stream().noneMatch(updated -> updated.getId() != null && updated.getId().equals(existing.getId()))
        );

        for (QuizQuestion updatedQuestion : updatedQuestions) {
            if (updatedQuestion.getId() == null ||
                existingQuestions.stream().noneMatch(existing -> existing.getId().equals(updatedQuestion.getId()))) {
                existingQuestions.add(updatedQuestion);
            } else {
                QuizQuestion existing = existingQuestions.stream()
                    .filter(q -> q.getId().equals(updatedQuestion.getId()))
                    .findFirst()
                    .orElseThrow();
                existing.setStatement(updatedQuestion.getStatement());
                existing.setHint(updatedQuestion.getHint());
                existing.setExplanation(updatedQuestion.getExplanation());
                existing.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
                existing.setOptions(updatedQuestion.getOptions());
                existing.setQuestionType(updatedQuestion.getQuestionType());
            }
        }

        quizRepository.save(quiz);
        return QuizAssembler.toDTO(quiz);
    }


    public QuizDTO delete(String id) {
        Quiz quiz = quizRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
        quizRepository.delete(quiz);
        return QuizAssembler.toDTO(quiz);
    }

    public QuizAttemptDTO submitAttempt(String quizId, Double score, Long timeSpent){
        QuizAttempt attempt = QuizAttempt.builder()
            .quiz(quizRepository.findById(UUID.fromString(quizId)).orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + quizId)))
            .user(userService.getCurrentUser())
            .score(score)
            .timeSpent(timeSpent)
            .completionDate(LocalDateTime.now())
            .build();

        quizAttemptRepository.save(attempt);
        return QuizAttemptAssembler.toDTO(attempt);
    }


}
