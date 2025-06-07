package com.studai.controller.quiz;

import com.studai.domain.quiz.attempt.dto.QuizAttemptDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.service.quiz.QuizService;
import com.studai.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

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

    @GetMapping("")
    public ResponseEntity<List<QuizDTO>> findAll(
            @RequestParam (required = false, defaultValue = "1") int page,
            @RequestParam (required = false, defaultValue = "10") int size
    ){
        List<QuizDTO> quizzes = quizService.find(page, size);
        return ResponseEntity.ok(quizzes);
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

    @PostMapping("/attempt")
    public ResponseEntity<QuizAttemptDTO> saveAttempt(@RequestBody QuizAttemptDTO body) {
        QuizAttemptDTO attempt = quizService.submitAttempt(body.getQuizId(), body.getScore(), body.getTimeSpent());
        return ResponseEntity.ok(attempt);
    }

}
