package com.studai.controller.quiz.question;

import com.studai.domain.quiz.question.dto.QuizQuestionDTO;
import com.studai.service.quiz.question.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/question")
public class QuizQuestionController {

    @Autowired
    private QuizQuestionService questionService;

    @PostMapping("")
    public ResponseEntity<QuizQuestionDTO> create(@RequestBody QuizQuestionDTO questionDTO) {
        QuizQuestionDTO question = questionService.create(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizQuestionDTO> findById(@PathVariable UUID id) {
        QuizQuestionDTO question = questionService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @PutMapping("")
    public ResponseEntity<QuizQuestionDTO> update(@RequestBody QuizQuestionDTO questionDTO) {
        QuizQuestionDTO question = questionService.update(questionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QuizQuestionDTO> delete(@PathVariable UUID id) {
        QuizQuestionDTO question = questionService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

}