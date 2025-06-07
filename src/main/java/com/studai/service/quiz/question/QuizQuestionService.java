package com.studai.service.quiz.question;

import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.repository.quiz.question.QuizQuestionRepository;
import com.studai.repository.quiz.QuizRepository;
import com.studai.utils.assembler.QuizQuestionAssembler;
import com.studai.utils.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuizQuestionService {

    private final QuizQuestionRepository questionRepository;
    private final QuizQuestionAssembler questionAssembler;

    public QuizQuestionService(
            QuizQuestionRepository questionRepository,
            QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.questionAssembler = new QuizQuestionAssembler(quizRepository);
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

    public QuizQuestionDTO update(QuizQuestionDTO questionDTO) {
        QuizQuestion existingQuestion = questionRepository.findById(questionDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionDTO.getId()));
        QuizQuestion updatedQuestion = questionAssembler.updateEntityFromDto(questionDTO, existingQuestion);

        return questionAssembler.toDto(questionRepository.save(updatedQuestion));
    }

    public QuizQuestionDTO delete(UUID id) {
        QuizQuestion entity = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + id));
        questionRepository.delete(entity);
        return questionAssembler.toDto(entity);
    }

}
