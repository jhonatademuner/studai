package com.studai.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN("Admin - Oversees the platform, manages users, and configures settings."),
    EDUCATOR("Educator - Creates and manages quizzes for students and assigns them."),
    STUDENT("Student - Creates personal quizzes and completes quizzes assigned by educators."),
    GUEST("Guest - Limited access to explore the platform (semi-student)."),
    DEVELOPER("Developer - Internal role for testing and debugging.");

    private final String description;

}
