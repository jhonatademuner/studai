package com.studai.utils.assembler;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizAssembler extends AbstractAssembler<Quiz, QuizDTO>  {

    private final UserService userService;
    private final QuizQuestionAssembler questionAssembler;

    public Quiz toEntity(QuizDTO dto){
		Quiz quiz = Quiz.builder()
			.id(dto.getId())
			.title(dto.getTitle())
			.description(dto.getDescription())
			.languageCode(dto.getLanguageCode())
			.sourceType(dto.getSourceType())
			.sourceContent(dto.getSourceContent())
			.user(userService.getCurrentUser())
			.build();

		questionAssembler.toEntityList(dto.getQuestions())
				.forEach(quiz::addQuestion);

		return quiz;
    }

    public QuizDTO toDto(Quiz entity) {
        return QuizDTO.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .sourceType(entity.getSourceType())
            .sourceContent(entity.getSourceContent())
            .questions(questionAssembler.toDtoList(entity.getQuestions()))
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
            .build();
    }

}
