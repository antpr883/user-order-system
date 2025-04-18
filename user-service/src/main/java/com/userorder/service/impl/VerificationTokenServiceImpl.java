package com.userorder.service.impl;

import com.userorder.persistance.model.VerificationToken;
import com.userorder.service.dto.VerificationTokenDTO;

import com.userorder.persistance.model.User;
import com.userorder.persistance.repository.UserRepository;
import com.userorder.persistance.repository.VerificationTokenRepository;
import com.userorder.persistance.utils.GraphBuilderMappingService;
import com.userorder.service.VerificationTokenService;
import com.userorder.service.dto.mappers.MappingOptions;
import com.userorder.service.dto.mappers.VerificationTokenMapper;
import com.userorder.service.exception.InvalidTokenException;
import com.userorder.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class VerificationTokenServiceImpl extends AbstractBaseService<VerificationToken, VerificationTokenDTO, VerificationTokenRepository> implements VerificationTokenService {

    private final VerificationTokenMapper tokenMapper;
    private final UserRepository userRepository;

    @Autowired
    public VerificationTokenServiceImpl(
            VerificationTokenRepository repository,
            GraphBuilderMappingService graphBuilderService,
            EntityManager entityManager,
            VerificationTokenMapper tokenMapper,
            UserRepository userRepository) {
        super(repository, graphBuilderService, entityManager, VerificationToken.class);
        this.tokenMapper = tokenMapper;
        this.userRepository = userRepository;
    }

    @Override
    protected Object getMapper() {
        return tokenMapper;
    }

    @Override
    protected VerificationTokenDTO toDto(VerificationToken entity, MappingOptions options) {
        return tokenMapper.toDtoWithOptions(entity, options);
    }

    @Override
    protected VerificationToken toEntity(VerificationTokenDTO dto) {
        return tokenMapper.toEntity(dto);
    }

    @Override
    protected void updateEntityFromDto(VerificationTokenDTO dto, VerificationToken entity, MappingOptions options) {
        tokenMapper.updateTokenFromDto(dto, entity);
    }

    @Override
    @Transactional
    public VerificationTokenDTO createToken(Long userId, VerificationToken.TokenType tokenType, int expirationMinutes) {
        log.debug("Creating {} token for user ID: {}", tokenType, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Generate a unique token
        String tokenString = UUID.randomUUID().toString();

        // Calculate expiry date
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(expirationMinutes);

        // Create token entity
        VerificationToken token = VerificationToken.builder()
                .token(tokenString)
                .user(user)
                .tokenType(tokenType)
                .expiryDate(expiryDate)
                .isUsed(false)
                .build();

        // Save token
        token = repository.save(token);

        return toDto(token, MappingOptions.builder().build());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        log.debug("Validating token: {}", token);

        return repository.findByToken(token)
                .map(verificationToken -> !verificationToken.isExpired() && !verificationToken.getIsUsed())
                .orElse(false);
    }

    @Override
    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void deleteExpiredTokens() {
        log.debug("Deleting expired tokens");
        repository.deleteByExpiryDateBefore(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationTokenDTO findByToken(String token) {
        log.debug("Finding token: {}", token);

        VerificationToken verificationToken = repository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found: " + token));

        return toDto(verificationToken, MappingOptions.builder().build());
    }

    @Override
    @Transactional
    public void markTokenAsUsed(String token) {
        log.debug("Marking token as used: {}", token);

        VerificationToken verificationToken = repository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found: " + token));

        verificationToken.setIsUsed(true);
        repository.save(verificationToken);
    }
}