package com.studai.utils.assembler;

import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.answer.QuizAttemptAnswer;
import com.studai.domain.quiz.attempt.answer.dto.QuizAttemptAnswerDTO;
import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.repository.quiz.question.QuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizAttemptAnswerAssembler extends AbstractAssembler<QuizAttemptAnswer, QuizAttemptAnswerDTO>{

	private final QuizAttemptRepository attemptRepository;
	private final QuizQuestionRepository questionRepository;

	@Override
	public QuizAttemptAnswerDTO toDto(QuizAttemptAnswer entity) {
		return QuizAttemptAnswerDTO.builder()
				.id(entity.getId())
				.attemptId(entity.getAttempt() != null ? entity.getAttempt().getId() : null)
				.questionId(entity.getQuestion() != null ? entity.getQuestion().getId() : null)
				.answer(entity.getAnswer())
				.correct(entity.isCorrect())
				.build();
	}

	@Override
	public QuizAttemptAnswer toEntity(QuizAttemptAnswerDTO dto) {
		QuizAttempt attempt = dto.getAttemptId() == null ? null : attemptRepository.findById(dto.getAttemptId())
				.orElseThrow(() -> new IllegalArgumentException("Quiz attempt not found: " + dto.getAttemptId()));

		QuizQuestion question = dto.getQuestionId() == null ? null : questionRepository.findById(dto.getQuestionId())
				.orElseThrow(() -> new IllegalArgumentException("Quiz question not found: " + dto.getQuestionId()));

		return QuizAttemptAnswer.builder()
				.id(dto.getId())
				.attempt(attempt)
				.question(question)
				.answer(dto.getAnswer())
				.correct(dto.isCorrect())
				.build();
	}
}
