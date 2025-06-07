package com.studai.service.quiz.attempt;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuizAttemptAssembler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuizAttemptService {

	private final QuizRepository quizRepository;
	private final UserService userService;
	private final QuizAttemptRepository quizAttemptRepository;
	private final QuizAttemptAssembler quizAttemptAssembler;

	public QuizAttemptService(
			QuizAttemptRepository quizAttemptRepository,
			QuizRepository quizRepository,
			UserService userService) {
		this.quizRepository = quizRepository;
		this.userService = userService;
		this.quizAttemptRepository = quizAttemptRepository;
		this.quizAttemptAssembler = new QuizAttemptAssembler(quizRepository, userService);
	}

	public QuizAttemptDTO create(QuizAttemptDTO dto) {
		QuizAttempt entity = quizAttemptAssembler.toEntity(dto);
		return quizAttemptAssembler.toDto(quizAttemptRepository.save(entity));
	}

	public QuizAttemptDTO findById(UUID id) {
		QuizAttempt entity = quizAttemptRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Quiz attempt not found with ID: " + id));
		return quizAttemptAssembler.toDto(entity);
	}

	public List<QuizAttemptDTO> findByQuizId(UUID quizId, int page, int size) {
		quizRepository.findById(quizId)
				.orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quizId));
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUserIdAndQuizId(userService.getCurrentUser().getId(), quizId, pageable);
		return quizAttemptAssembler.toDtoList(quizAttempts);
	}

}
