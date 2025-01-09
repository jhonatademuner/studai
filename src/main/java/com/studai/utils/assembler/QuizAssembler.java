package com.studai.utils.assembler;

import com.studai.domain.question.Question;
import com.studai.domain.question.dto.QuestionDTO;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.dto.QuizDTO;

import java.util.List;

public class QuizAssembler {

    public static Quiz toEntity(QuizDTO dto){

        Quiz quiz = Quiz.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .build();

        List<Question> questions = QuestionAssembler.toEntityList(dto.getQuestions(), quiz);
        quiz.setQuestions(questions);
        return quiz;
    }

    public static QuizDTO toDTO(Quiz quiz) {

        List<QuestionDTO> questionDTOs = QuestionAssembler.toDTOList(quiz.getQuestions());

        return QuizDTO.builder()
            .id(quiz.getId().toString())
            .title(quiz.getTitle())
            .description(quiz.getDescription())
            .questions(questionDTOs)
            .build();
    }

}
