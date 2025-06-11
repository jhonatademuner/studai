package com.studai.service.quiz.attempt;

import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.quiz.attempt.answer.dto.QuizAttemptAnswerDTO;
import com.studai.domain.quiz.attempt.dto.QuizAttemptCreateDTO;
import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.question.QuizQuestion;
import com.studai.repository.quiz.QuizRepository;
import com.studai.repository.quiz.attempt.QuizAttemptRepository;
import com.studai.repository.quiz.question.QuizQuestionRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuizAttemptAnswerAssembler;
import com.studai.utils.assembler.QuizAttemptAssembler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuizAttemptService {

	private final QuizRepository quizRepository;
	private final UserService userService;
	private final QuizAttemptRepository quizAttemptRepository;
	private final QuizAttemptAssembler quizAttemptAssembler;

	public QuizAttemptService(
			QuizAttemptRepository quizAttemptRepository,
			QuizQuestionRepository quizQuestionRepository,
			QuizRepository quizRepository,
			UserService userService) {
		this.quizRepository = quizRepository;
		this.userService = userService;
		QuizAttemptAnswerAssembler quizAttemptAnswerAssembler = new QuizAttemptAnswerAssembler(quizAttemptRepository, quizQuestionRepository);
		this.quizAttemptRepository = quizAttemptRepository;
		this.quizAttemptAssembler = new QuizAttemptAssembler(quizRepository, userService, quizAttemptAnswerAssembler);
	}

	public QuizAttemptDTO create(QuizAttemptCreateDTO dto) {
		Map<UUID, String> correctAnswers = this.getCorrectAnswers(dto.getQuizId());
		List<QuizAttemptAnswerDTO> answers = this.assembleAnswersDTOs(dto.getAnswers(), correctAnswers);
		BigDecimal score = this.calculateScore(answers);
		QuizAttemptDTO quizAttemptDTO = QuizAttemptDTO.builder()
				.quizId(dto.getQuizId())
				.score(score)
				.timeSpent(dto.getTimeSpent())
				.answers(answers)
				.guestName(dto.getGuestName())
				.guestUser(dto.isGuestUser())
				.build();
		QuizAttempt entity = quizAttemptAssembler.toEntity(quizAttemptDTO);
		return quizAttemptAssembler.toDto(quizAttemptRepository.save(entity));
	}

	public QuizAttemptDTO findById(UUID id) {
		QuizAttempt entity = quizAttemptRepository.findByUserIdAndId(userService.getCurrentUser().getId(), id)
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

	private Map<UUID, String> getCorrectAnswers(UUID quizId) {
		List<QuizQuestion> questions = quizRepository.findById(quizId)
				.orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quizId))
				.getQuestions();

		return questions.stream()
				.collect(Collectors.toMap(
						QuizQuestion::getId,
						QuizQuestion::getCorrectAnswer
				));
	}

	private List<QuizAttemptAnswerDTO> assembleAnswersDTOs(Map<UUID, String> answers, Map<UUID, String> correctAnswers) {
		return answers.entrySet().stream()
				.map(entry -> {
					UUID questionId = entry.getKey();
					String answer = entry.getValue();
					boolean isCorrect = correctAnswers.get(questionId).equals(answer);
					return QuizAttemptAnswerDTO.builder()
							.questionId(questionId)
							.answer(answer)
							.correct(isCorrect)
							.build();
				})
				.collect(Collectors.toList());
	}

	private BigDecimal calculateScore(List<QuizAttemptAnswerDTO> answers) {
		if (answers.isEmpty()) {
			return BigDecimal.ZERO;
		}

		long correctCount = answers.stream()
				.filter(QuizAttemptAnswerDTO::isCorrect)
				.count();

		BigDecimal score = BigDecimal.valueOf(correctCount);
		score = score.divide(BigDecimal.valueOf(answers.size()), 10, RoundingMode.HALF_UP); // scale 10 for precision
		return score.multiply(BigDecimal.valueOf(100))
				.setScale(2, RoundingMode.HALF_UP); // round to 2 decimal places

	}


}
