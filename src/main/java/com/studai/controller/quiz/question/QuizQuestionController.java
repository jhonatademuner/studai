package com.studai.controller.quiz.question;

import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.service.quiz.question.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizQuestionController {

    private final QuizQuestionService questionService;

    @PostMapping("/v1/quiz/question")
    public ResponseEntity<QuizQuestionDTO> create(@RequestBody QuizQuestionDTO questionDTO) {
        QuizQuestionDTO question = questionService.create(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    @GetMapping("/v1/quiz/question/{id}")
    public ResponseEntity<QuizQuestionDTO> findById(@PathVariable UUID id) {
        QuizQuestionDTO question = questionService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @GetMapping("/v1/quiz/{quizId}/questions")
    public ResponseEntity<List<QuizQuestionDTO>> findByQuizId(
            @PathVariable UUID quizId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        List<QuizQuestionDTO> questions = questionService.findByQuizId(quizId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

    @PutMapping("/v1/quiz/question")
    public ResponseEntity<QuizQuestionDTO> update(@RequestBody QuizQuestionDTO questionDTO) {
        QuizQuestionDTO question = questionService.update(questionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @DeleteMapping("/v1/quiz/question/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        questionService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}