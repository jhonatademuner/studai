package com.studai.services.quiz;

import com.studai.client.assistant.AssistantClient;
import com.studai.domain.quiz.dto.QuizDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QuizService {

    @Autowired
    private AssistantClient assistantClient;

    public QuizDTO generateQuiz(String videoId, int questionsNumber, String language) {
        Map<String, String> headers = Map.of("Connection", "keep-alive");
        Map<String, String> params = Map.of("videoId", videoId, "questionsNumber", String.valueOf(questionsNumber), "language", language);

        return assistantClient.postRequest("/quiz", null, headers, params, QuizDTO.class);
    }

}
