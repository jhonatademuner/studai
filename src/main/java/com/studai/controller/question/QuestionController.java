package com.studai.controller.question;

import com.studai.domain.question.dto.QuestionDTO;
import com.studai.service.question.QuestionService;
import com.studai.utils.assembler.QuestionAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("")
    public ResponseEntity<QuestionDTO> create(@RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestion = questionService.create(questionDTO);
        return ResponseEntity.ok(createdQuestion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> findById(@PathVariable String id) {
        QuestionDTO question = questionService.findById(id);
        return ResponseEntity.ok(question);
    }

    @PutMapping("")
    public ResponseEntity<QuestionDTO> update(@RequestBody QuestionDTO questionDTO) {
        QuestionDTO question = questionService.update(questionDTO);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QuestionDTO> delete(@PathVariable String id) {
        QuestionDTO question = questionService.delete(id);
        return ResponseEntity.ok(question);
    }

}