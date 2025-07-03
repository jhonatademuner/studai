package com.studai.repository.quiz.question;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.question.QuizQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, UUID> {
	List<QuizQuestion> findByQuiz(Quiz quiz, Pageable pageable);
}
