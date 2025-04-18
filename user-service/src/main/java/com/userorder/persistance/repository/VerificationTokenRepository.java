package com.userorder.persistance.repository;

import com.userorder.persistance.model.User;
import com.userorder.persistance.model.VerificationToken;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends BaseCustomJpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByTokenAndUser(String token, User user);

    List<VerificationToken> findByUserAndTokenTypeAndIsUsed(User user, VerificationToken.TokenType tokenType, boolean isUsed);

    void deleteByExpiryDateBefore(LocalDateTime date);
}

