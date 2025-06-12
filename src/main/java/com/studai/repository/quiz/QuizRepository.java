package com.studai.repository.quiz;

import com.studai.domain.quiz.Quiz;
import com.studai.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    List<Quiz> findByUser(User user, Pageable pageable);
    Optional<Quiz> findByIdAndUser(UUID id, User user);
}
