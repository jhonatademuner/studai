package com.studai.domain.quiz;

import com.studai.domain.quiz.attempt.QuizAttempt;
import com.studai.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import com.studai.domain.quiz.question.QuizQuestion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tquiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuizLanguage languageCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuizSourceType sourceType;

    @Column(nullable = false)
    private String sourceContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuizQuestion> questions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addQuestion(QuizQuestion question) {
        question.setQuiz(this);
        this.questions.add(question);
    }

    public void removeQuestion(QuizQuestion question) {
        question.setQuiz(null);
        this.questions.remove(question);
    }

}
