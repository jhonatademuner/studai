package com.studai.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studai.domain.quiz.QuizLanguage;
import com.studai.domain.quiz.QuizSourceType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizCreateDTO {

	private int questionsQuantity;
	private String sourceContent;
	private QuizSourceType sourceType;
	private QuizLanguage languageCode;

}
