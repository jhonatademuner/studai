package com.studai.service.quiz.question;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.repository.quiz.question.QuizQuestionRepository;
import com.studai.repository.quiz.QuizRepository;
import com.studai.utils.assembler.QuizQuestionAssembler;
import com.studai.utils.exception.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuizQuestionService {

    private final QuizQuestionRepository questionRepository;
    private final QuizQuestionAssembler questionAssembler;
    private final QuizRepository quizRepository;

    public QuizQuestionService(
            QuizQuestionRepository questionRepository,
            QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.questionAssembler = new QuizQuestionAssembler(quizRepository);
        this.quizRepository = quizRepository;
    }

    public QuizQuestionDTO create(QuizQuestionDTO dto){
        QuizQuestion entity = questionRepository.save(questionAssembler.toEntity(dto));
        return questionAssembler.toDto(entity);
    }

    public QuizQuestionDTO findById(UUID id){
        QuizQuestion entity = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + id));
        return questionAssembler.toDto(entity);
    }

    public List<QuizQuestionDTO> findByQuizId(UUID quizId, int page, int size) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + quizId));
        Pageable pageable = PageRequest.of(page, size);
        List<QuizQuestion> questions = questionRepository.findByQuiz(quiz, pageable);
        return questionAssembler.toDtoList(questions);
    }

    public QuizQuestionDTO update(QuizQuestionDTO questionDTO) {
        QuizQuestion existingQuestion = questionRepository.findById(questionDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionDTO.getId()));
        QuizQuestion updatedQuestion = questionAssembler.updateEntityFromDto(questionDTO, existingQuestion);

        return questionAssembler.toDto(questionRepository.save(updatedQuestion));
    }

    public void delete(UUID id) {
        QuizQuestion entity = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + id));
        questionRepository.delete(entity);
    }

}
