package com.userorder.service;

import com.userorder.persistance.model.VerificationToken;
import com.userorder.service.dto.VerificationTokenDTO;

public interface VerificationTokenService {
    /**
     * Creates a new verification token for a user
     *
     * @param userId User ID to create token for
     * @param tokenType Type of token (EMAIL_VERIFICATION, PASSWORD_RESET)
     * @param expirationMinutes Expiration time in minutes
     * @return Created token DTO
     */
    VerificationTokenDTO createToken(Long userId, VerificationToken.TokenType tokenType, int expirationMinutes);

    /**
     * Validates a token
     *
     * @param token Token to validate
     * @return true if token is valid
     */
    boolean validateToken(String token);

    /**
     * Deletes all expired tokens
     */
    void deleteExpiredTokens();

    /**
     * Finds a token by its string value
     *
     * @param token Token string
     * @return Token DTO
     */
    VerificationTokenDTO findByToken(String token);

    /**
     * Marks a token as used
     *
     * @param token Token string
     */
    void markTokenAsUsed(String token);
}