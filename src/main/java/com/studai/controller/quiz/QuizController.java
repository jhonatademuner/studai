package com.studai.controller.quiz;

import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.service.quiz.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("")
    public ResponseEntity<QuizDTO> create(@RequestParam String videoId,
                                          @RequestParam int questionsNumber,
                                          @RequestParam String language) {
        QuizDTO quiz = quizService.create(videoId, questionsNumber, language);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> findById(@PathVariable String id){
        QuizDTO quiz = quizService.findById(id);
        return ResponseEntity.ok(quiz);
    }

    @PutMapping("")
    public ResponseEntity<QuizDTO> update(@RequestBody QuizDTO quizDTO){
        QuizDTO quiz = quizService.update(quizDTO);
        return ResponseEntity.ok(quiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QuizDTO> delete(@PathVariable String id){
        QuizDTO quiz = quizService.delete(id);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/generate")
    public ResponseEntity<QuizDTO> generateQuiz(@RequestParam String videoId,
                                                @RequestParam int questionsNumber,
                                                @RequestParam String language) {
        QuizDTO quiz = quizService.generateQuiz(videoId, questionsNumber, language);
        return ResponseEntity.ok(quiz);
    }

}
