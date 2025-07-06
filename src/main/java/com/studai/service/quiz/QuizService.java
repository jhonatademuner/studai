package com.studai.service.quiz;

import com.studai.client.assistant.AssistantClient;
import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.QuizLanguage;
import com.studai.domain.quiz.QuizSourceType;
import com.studai.domain.quiz.dto.QuizCreateDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.repository.quiz.QuizRepository;
import com.studai.service.user.UserService;
import com.studai.utils.assembler.QuizAssembler;
import com.studai.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizService {

	private final QuizRepository quizRepository;
	private final QuizAssembler quizAssembler;
	private final UserService userService;
	private final AssistantClient assistantClient;

	public QuizDTO create(QuizCreateDTO quizCreateDTO) {
		QuizDTO quizDTO = this.generateQuiz(quizCreateDTO);
		this.fillQuizAdditionalFields(quizDTO, quizCreateDTO);
		Quiz entity = quizAssembler.toEntity(quizDTO);
		return quizAssembler.toDto(quizRepository.save(entity));
	}

	public QuizDTO createFromFile(MultipartFile file, QuizSourceType sourceType,
	  int questionsQuantity, QuizLanguage languageCode, int startPage, int endPage) {
		QuizDTO quizDTO = this.generateQuizFromFile(file, sourceType, questionsQuantity, languageCode, startPage, endPage);
		quizDTO.setSourceContent("This quiz was generated from a file, unfortunately we don't support file saving currently.");
		quizDTO.setSourceType(sourceType);
		quizDTO.setLanguageCode(languageCode);
		Quiz entity = quizAssembler.toEntity(quizDTO);
		return quizAssembler.toDto(quizRepository.save(entity));
	}

	public QuizDTO findById(UUID id) {
		Quiz quiz = quizRepository.findByIdAndUser(id, userService.getCurrentUser())
				.orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
		return quizAssembler.toDto(quiz);
	}

	public QuizDTO findByIdForGuest(UUID id) {
		Quiz quiz = quizRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
		return quizAssembler.toDto(quiz);
	}

	public List<QuizDTO> find(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		List<Quiz> quizzes = quizRepository.findByUser(userService.getCurrentUser(), pageable);
		return quizAssembler.toDtoList(quizzes);
	}

	public void delete(UUID id) {
		Quiz quiz = quizRepository.findByIdAndUser(id, userService.getCurrentUser())
				.orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + id));
		quizRepository.delete(quiz);
	}

	private QuizDTO generateQuiz(QuizCreateDTO quizCreateDTO) {
		return assistantClient.postRequest("/api/v1/quizzes", quizCreateDTO, null, null, QuizDTO.class).getBody();
	}

	private QuizDTO generateQuizFromFile(MultipartFile file, QuizSourceType sourceType,
		int questionsQuantity, QuizLanguage languageCode, int startPage, int endPage) {

		try {
			File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
			file.transferTo(tempFile);

			Map<String, String> formFields = Map.of(
					"sourceType", String.valueOf(sourceType),
					"questionsQuantity", String.valueOf(questionsQuantity),
					"languageCode", String.valueOf(languageCode),
					"startPage", String.valueOf(startPage),
					"endPage", String.valueOf(endPage)
			);

			return assistantClient
					.postMultipartRequest("/api/v1/quizzes/from-file", tempFile, formFields, null, QuizDTO.class)
					.getBody();
		} catch (IOException e) {
			throw new RuntimeException("Failed to send multipart request", e);
		}
	}

	private void fillQuizAdditionalFields(QuizDTO quizDTO, QuizCreateDTO quizCreateDTO) {
		quizDTO.setSourceContent(quizCreateDTO.getSourceContent());
		quizDTO.setSourceType(quizCreateDTO.getSourceType());
		quizDTO.setLanguageCode(quizCreateDTO.getLanguageCode());
	}

}
