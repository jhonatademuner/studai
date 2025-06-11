package com.studai.domain.quiz.attempt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizAttemptCreateDTO {

	private UUID quizId;
	private Long timeSpent;
	private boolean guestUser;
	private String guestName;
	private Map<UUID, String> answers = new HashMap<>();

}
