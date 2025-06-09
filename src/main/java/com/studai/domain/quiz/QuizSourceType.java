package com.studai.domain.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuizSourceType {

    YOUTUBE_VIDEO("From a YouTube video via its transcript, audio, or metadata."),

    FILE_UPLOAD("From an uploaded file (e.g., PDF, DOCX, TXT)."),

    PROMPT_BASED("From a user-written prompt with specific instructions. "
            + "Example: 'Create a quiz about the Cold War, focusing on key events and political figures, "
            + "targeted at high school students.'"),

    TOPIC_BASED("From a broad thematic topic (e.g., 'Cloud Computing', 'European History').");

    private final String description;

}
