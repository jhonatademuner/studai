package com.studai.domain.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuizSourceType {

    YOUTUBE_VIDEO("From a YouTube video via its transcript, audio, or metadata."),

    PDF_CONTENT("From a PDF file, extracting text from specified pages."),

    PROMPT_BASED("From a user-written prompt with specific instructions. "
            + "Example: 'Create a quiz about the Cold War, focusing on key events and political figures, "
            + "targeted at high school students.'");

    private final String description;

}
