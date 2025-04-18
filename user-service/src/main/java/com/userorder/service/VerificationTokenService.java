package com.userorder.service;

import com.userorder.persistance.model.VerificationToken;
import com.userorder.service.dto.VerificationTokenDTO;

public interface VerificationTokenService {
    VerificationTokenDTO createToken(Long userId, VerificationToken.TokenType tokenType, int expirationMinutes);
    boolean validateToken(String token);
    void deleteExpiredTokens();
    VerificationTokenDTO findByToken(String token);
    void markTokenAsUsed(String token);
}