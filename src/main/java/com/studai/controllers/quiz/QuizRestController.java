package com.studai.controllers.quiz;

import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.services.quiz.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz")
public class QuizRestController {

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
