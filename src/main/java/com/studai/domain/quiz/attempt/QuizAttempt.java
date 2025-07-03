package com.studai.domain.quiz.attempt;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.quiz.attempt.answer.QuizAttemptAnswer;
import com.studai.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
@Table(name = "tquiz_attempt")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false, updatable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true, updatable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private BigDecimal score;

    @Column(nullable = false, updatable = false)
    private Long timeSpent; // Seconds spent on the attempt

    @Column(nullable = false, updatable = false)
    private boolean guestUser;

    @Column(updatable = false)
    private String guestName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<QuizAttemptAnswer> answers = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void addAnswer(QuizAttemptAnswer answer) {
        answers.add(answer);
        answer.setAttempt(this);
    }

    public void removeAnswer(QuizAttemptAnswer answer) {
        answers.remove(answer);
        answer.setAttempt(null);
    }

}