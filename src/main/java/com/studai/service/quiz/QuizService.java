package com.studai.service.quiz;

import com.studai.client.assistant.AssistantClient;
import com.studai.domain.question.Question;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.QuizSourceType;
import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.domain.user.User;
import com.studai.repository.question.QuestionRepository;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuestionAssembler;
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
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AssistantClient assistantClient;

    public QuizDTO generateQuiz(String videoId, int questionsNumber, String language) {
        Map<String, String> headers = Map.of("Connection", "keep-alive");
        Map<String, String> params = Map.of("videoId", videoId, "questionsNumber", String.valueOf(questionsNumber), "language", language);

        return assistantClient.postRequest("/quiz", null, headers, params, QuizDTO.class);
    }

    public QuizDTO create(String videoId, int questionsNumber, String language){
        QuizDTO quizDTO = generateQuiz(videoId, questionsNumber, language);
        Quiz entity = QuizAssembler.toEntity(quizDTO, userService.getCurrentUser());
        entity.setSourceType(QuizSourceType.YOUTUBE_VIDEO);
        entity.setSourceUri(videoId);
        quizRepository.save(entity);
        return QuizAssembler.toDTO(entity);
    }

    public QuizDTO findById(String id){
        Quiz quiz = quizRepository.findById(UUID.fromString(id)).orElseThrow(ResourceNotFoundException::new);
        return QuizAssembler.toDTO(quiz);
    }

    public List<QuizDTO> findAll() {
        List<Quiz> quizzes = quizRepository.findByUser(userService.getCurrentUser());
        if(quizzes.isEmpty()) throw new ResourceNotFoundException();
        List<QuizDTO> quizDtos = new ArrayList<>();
        for(Quiz quiz : quizzes){
            quizDtos.add(QuizAssembler.toDTO(quiz));
        }
        return quizDtos;
    }

    public QuizDTO update(QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(UUID.fromString(quizDTO.getId()))
            .orElseThrow(ResourceNotFoundException::new);

        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());

        List<Question> updatedQuestions = QuestionAssembler.toEntityList(quizDTO.getQuestions(), quiz);
        List<Question> existingQuestions = quiz.getQuestions();

        existingQuestions.removeIf(existing ->
            updatedQuestions.stream().noneMatch(updated -> updated.getId() != null && updated.getId().equals(existing.getId()))
        );

        for (Question updatedQuestion : updatedQuestions) {
            if (updatedQuestion.getId() == null ||
                existingQuestions.stream().noneMatch(existing -> existing.getId().equals(updatedQuestion.getId()))) {
                existingQuestions.add(updatedQuestion);
            } else {
                Question existing = existingQuestions.stream()
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
        Quiz quiz = quizRepository.findById(UUID.fromString(id)).orElseThrow(ResourceNotFoundException::new);
        quizRepository.delete(quiz);
        return QuizAssembler.toDTO(quiz);
    }

    public QuizAttemptDTO submitAttempt(String quizId, Double score, Long timeSpent){
        QuizAttempt attempt = QuizAttempt.builder()
            .quiz(quizRepository.findById(UUID.fromString(quizId)).orElseThrow(ResourceNotFoundException::new))
            .user(userService.getCurrentUser())
            .score(score)
            .timeSpent(timeSpent)
            .completionDate(LocalDateTime.now())
            .build();

        quizAttemptRepository.save(attempt);
        return QuizAttemptAssembler.toDTO(attempt);
    }


}
