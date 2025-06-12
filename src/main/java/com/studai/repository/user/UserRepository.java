package com.studai.repository.user;

import com.studai.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameAndActiveTrue(String username);
    User findByEmailAndActiveTrue(String email);
}
