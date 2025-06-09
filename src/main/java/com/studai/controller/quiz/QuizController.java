package com.studai.controller.quiz;

import com.studai.domain.quiz.dto.QuizCreateDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.service.quiz.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/v1/quiz")
    public ResponseEntity<QuizDTO> create(@RequestBody QuizCreateDTO quizCreateDTO) {
        QuizDTO quiz = quizService.create(quizCreateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(quiz);
    }

    @GetMapping("/v1/quiz/{id}")
    public ResponseEntity<QuizDTO> findById(@PathVariable UUID id){
        QuizDTO quiz = quizService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(quiz);
    }

    @GetMapping("/v1/quiz")
    public ResponseEntity<List<QuizDTO>> findAll(
            @RequestParam (required = false, defaultValue = "0") int page,
            @RequestParam (required = false, defaultValue = "10") int size
    ){
        List<QuizDTO> quizzes = quizService.find(page, size);
        return ResponseEntity.ok(quizzes);
    }

    @DeleteMapping("/v1/quiz/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        quizService.delete(id);
        return ResponseEntity.ok().build();
    }

}
