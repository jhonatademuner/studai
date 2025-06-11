package com.studai.domain.quiz.attempt.answer;

import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.question.QuizQuestion;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tquiz_attempt_answer")
public class QuizAttemptAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attempt_id", nullable = false, updatable = false)
	private QuizAttempt attempt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false, updatable = false)
	private QuizQuestion question;

	@Column(nullable = false, updatable = false)
	private String answer;

	@Column(nullable = false)
	private boolean correct;

}
