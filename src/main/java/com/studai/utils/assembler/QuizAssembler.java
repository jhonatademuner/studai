package com.studai.utils.assembler;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuizAssembler extends AbstractAssembler<Quiz, QuizDTO>  {

    private final UserService userService;
    private final QuizQuestionAssembler questionAssembler;
    private final QuizAttemptAssembler quizAttemptAssembler;

    public Quiz toEntity(QuizDTO dto){
		return Quiz.builder()
			.id(dto.getId() != null ? dto.getId() : null)
			.title(dto.getTitle())
			.description(dto.getDescription())
			.sourceType(dto.getSourceType() != null ? dto.getSourceType() : null)
			.sourceUri(dto.getSourceUri() != null ? dto.getSourceUri() : null)
			.user(userService.getCurrentUser())
			.build();
    }

    public QuizDTO toDto(Quiz entity) {
        return QuizDTO.builder()
            .id(entity.getId() != null ? entity.getId() : null)
            .title(entity.getTitle())
            .description(entity.getDescription())
            .sourceType(entity.getSourceType())
            .sourceUri(entity.getSourceUri())
            .userId(entity.getUser() != null ? entity.getUser().getId().toString() : null)
            .questions(questionAssembler.toDtoList(entity.getQuestions()))
            .attempts(quizAttemptAssembler.toDtoList(entity.getAttempts()))
            .build();
    }

}
