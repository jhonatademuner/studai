package com.studai.domain.quiz.attempt.answer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizAttemptAnswerDTO {

	private UUID id;
	private UUID attemptId;
	private UUID questionId;
	private String answer;
	private boolean correct;

}
