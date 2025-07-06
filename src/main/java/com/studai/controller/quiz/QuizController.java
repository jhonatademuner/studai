package com.studai.controller.quiz;

import com.studai.domain.quiz.QuizLanguage;
import com.studai.domain.quiz.QuizSourceType;
import com.studai.domain.quiz.dto.QuizCreateDTO;
import com.studai.domain.quiz.dto.QuizDTO;
import com.studai.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/v1/quiz")
    public ResponseEntity<QuizDTO> create(@RequestBody QuizCreateDTO quizCreateDTO) {
        QuizDTO quiz = quizService.create(quizCreateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(quiz);
    }

    @PostMapping(value = "/v1/quiz/from-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuizDTO> createFromFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("sourceType") QuizSourceType sourceType,
            @RequestParam(name = "questionsQuantity", defaultValue = "10") int questionsQuantity,
            @RequestParam(name = "languageCode", defaultValue = "EN") QuizLanguage languageCode,
            @RequestParam(name = "startPage", defaultValue = "1") int startPage,
            @RequestParam(name = "endPage", defaultValue = "25") int endPage
    ) {
        QuizDTO quiz = quizService.createFromFile(file, sourceType, questionsQuantity, languageCode, startPage, endPage);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/v1/quiz/{id}")
    public ResponseEntity<QuizDTO> findById(@PathVariable UUID id){
        QuizDTO quiz = quizService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(quiz);
    }

    @GetMapping("/v1/guest/quiz/{id}")
    public ResponseEntity<QuizDTO> findByIdForGuest(@PathVariable UUID id){
        QuizDTO quiz = quizService.findByIdForGuest(id);
        return ResponseEntity.status(HttpStatus.OK).body(quiz);
    }

    @GetMapping("/v1/quiz")
    public ResponseEntity<List<QuizDTO>> findAll(
            @RequestParam (required = false, defaultValue = "0") int page,
            @RequestParam (required = false, defaultValue = "10") int size
    ){
        List<QuizDTO> quizzes = quizService.find(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(quizzes);
    }

    @DeleteMapping("/v1/quiz/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        quizService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
