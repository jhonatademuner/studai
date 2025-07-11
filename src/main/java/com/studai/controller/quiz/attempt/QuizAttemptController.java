package com.studai.controller.quiz.attempt;

import com.studai.domain.quiz.attempt.dto.QuizAttemptCreateDTO;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.service.quiz.attempt.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizAttemptController {

	private final QuizAttemptService quizAttemptService;

	@PostMapping("/v1/quiz/attempt")
	public ResponseEntity<QuizAttemptDTO> create(@RequestBody QuizAttemptCreateDTO createDTO) {
		QuizAttemptDTO attempt = quizAttemptService.create(createDTO, false);
		return ResponseEntity.status(HttpStatus.CREATED).body(attempt);
	}

	@PostMapping("/v1/guest/quiz/attempt")
	public ResponseEntity<QuizAttemptDTO> guestCreate(@RequestBody QuizAttemptCreateDTO createDTO) {
		QuizAttemptDTO attempt = quizAttemptService.create(createDTO, true);
		return ResponseEntity.status(HttpStatus.CREATED).body(attempt);
	}

	@GetMapping("/v1/quiz/attempt/{id}")
	public ResponseEntity<QuizAttemptDTO> findById(@PathVariable UUID id) {
		QuizAttemptDTO attempt = quizAttemptService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(attempt);
	}

	@GetMapping("/v1/quiz/{quizId}/attempts")
	public ResponseEntity<List<QuizAttemptDTO>> findByQuizId(
			@PathVariable UUID quizId,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size
	) {
		return ResponseEntity.status(HttpStatus.OK).body(quizAttemptService.findByQuizId(quizId, page, size));
	}


}
