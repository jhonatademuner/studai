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

    @PostMapping("/generate")
    public ResponseEntity<QuizDTO> generateQuiz(@RequestParam String videoId,
                                                @RequestParam int questionsNumber,
                                                @RequestParam String language) {
        QuizDTO quiz = quizService.generateQuiz(videoId, questionsNumber, language);
        return ResponseEntity.ok(quiz);
    }

}
