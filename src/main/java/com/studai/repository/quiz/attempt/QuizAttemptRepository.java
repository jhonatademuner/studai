package com.studai.repository.quiz.attempt;

import com.studai.domain.quiz.attempt.QuizAttempt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {
	List<QuizAttempt> findByUserIdAndQuizId(UUID userId, UUID quizId, Pageable pageable);
	Optional<QuizAttempt> findByUserIdAndId(UUID userId, UUID id);
}
