package com.studai.service.question;

import com.studai.domain.question.Question;
import com.studai.domain.question.dto.QuestionDTO;
import com.studai.domain.quiz.Quiz;
import com.studai.repository.question.QuestionRepository;
import com.studai.repository.quiz.QuizRepository;
import com.studai.utils.assembler.QuestionAssembler;
import com.studai.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public QuestionDTO create(QuestionDTO dto){
        Quiz quiz = quizRepository.findById(UUID.fromString(dto.getQuizId())).orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + dto.getQuizId()));
        Question entity = questionRepository.save(QuestionAssembler.toEntity(dto, quiz));
        return QuestionAssembler.toDTO(entity);
    }

    public QuestionDTO findById(String id){
        Question entity = questionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + id));
        return QuestionAssembler.toDTO(entity);
    }

    public QuestionDTO update(QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(UUID.fromString(questionDTO.getId())).orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionDTO.getId()));
        Question updatedQuestion = QuestionAssembler.toEntity(questionDTO, existingQuestion.getQuiz());
        updatedQuestion.setId(existingQuestion.getId());

        return QuestionAssembler.toDTO(questionRepository.save(updatedQuestion));
    }

    public QuestionDTO delete(String id) {
        Question entity = questionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + id));
        questionRepository.delete(entity);
        return QuestionAssembler.toDTO(entity);
    }

}
