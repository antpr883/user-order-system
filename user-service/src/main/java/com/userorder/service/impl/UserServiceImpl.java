package com.userorder.service.impl;

import com.userorder.persistance.model.User;
import com.userorder.persistance.model.VerificationToken;
import com.userorder.persistance.repository.UserRepository;
import com.userorder.persistance.repository.VerificationTokenRepository;
import com.userorder.persistance.utils.GraphBuilderMappingService;
import com.userorder.service.UserService;
import com.userorder.service.VerificationTokenService;
import com.userorder.service.dto.PasswordChangeRequestDTO;
import com.userorder.service.dto.UserDTO;
import com.userorder.service.dto.base.OnCreate;
import com.userorder.service.dto.mappers.MappingOptions;
import com.userorder.service.dto.mappers.UserMapper;
import com.userorder.service.exception.InvalidTokenException;
import com.userorder.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl extends AbstractBaseService<User, UserDTO, UserRepository, UserMapper> implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final VerificationTokenService verificationTokenService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ValidationService validationService;


    @Autowired
    public UserServiceImpl(
            UserRepository repository,
            GraphBuilderMappingService graphBuilderService,
            EntityManager entityManager,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            VerificationTokenRepository tokenRepository,
            VerificationTokenService verificationTokenService,
            KafkaTemplate<String, Object> kafkaTemplate,
            ValidationService validationService) {
        super(repository, userMapper, graphBuilderService, entityManager);
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.verificationTokenService = verificationTokenService;
        this.kafkaTemplate = kafkaTemplate;
        this.validationService = validationService;
    }


    @Override
    @Cacheable(value = "usersByUsername", key = "#username + '-' + #includeAudit + '-' + (#attributes != null ? #attributes.hashCode() : 'basic')")
    @Transactional(readOnly = true)
    public UserDTO findByUsername(String username, boolean includeAudit, Set<String> attributes) {
        log.debug("Finding user by username {} with attributes: {}, includeAudit: {}", username, attributes, includeAudit);

        User user;
        if (attributes == null || attributes.isEmpty()) {
            user = repository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        } else {
            Specification<User> spec = (root, query, cb) -> cb.equal(root.get("username"), username);
            user = repository.findAllWithAttributes(spec, attributes)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        }

        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .includeAudit(includeAudit)
                .build();

        return  mapper.toDtoWithOptions(user, options);
    }

    @Override
    @Cacheable(value = "usersByEmail", key = "#email + '-' + #includeAudit + '-' + (#attributes != null ? #attributes.hashCode() : 'basic')")
    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email, boolean includeAudit, Set<String> attributes) {
        log.debug("Finding user by email {} with attributes: {}, includeAudit: {}", email, attributes, includeAudit);

        User user;
        if (attributes == null || attributes.isEmpty()) {
            user = repository.findByContactsEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        } else {
            Specification<User> spec = (root, query, cb) -> cb.equal(root.join("contacts").get("email"), email);
            user = repository.findAllWithAttributes(spec, attributes)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        }

        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .includeAudit(includeAudit)
                .build();

        return mapper.toDtoWithOptions(user, options);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    @CacheEvict(value = {"userCache", "usersByUsername", "usersByEmail"}, allEntries = true)
    @Transactional
    public UserDTO changePassword(PasswordChangeRequestDTO passwordChangeDto) {
        log.debug("Changing password for user with ID: {}", passwordChangeDto.getUserId());

        // Find the user
        User user = repository.findById(passwordChangeDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + passwordChangeDto.getUserId()));

        // Verify token using tokenRepository to get the token entity
        VerificationToken token = tokenRepository.findByTokenAndUser(passwordChangeDto.getToken(), user)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        // Check if token is expired or used
        if (token.isExpired() || token.getIsUsed()) {
            throw new InvalidTokenException("Token is expired or already used");
        }

        // Change password
        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));

        // Mark token as used
        token.setIsUsed(true);
        tokenRepository.save(token);

        // Save user
        User updatedUser = repository.save(user);

        // Publish password changed event
        kafkaTemplate.send("user-password-changed", Map.of(
                "userId", user.getId(),
                "timestamp", System.currentTimeMillis()
        ));

        // Return user data
        return mapper.toDtoWithOptions(updatedUser, MappingOptions.builder().build());
    }

    @Override
    @CacheEvict(value = {"userCache", "usersByUsername", "usersByEmail"}, allEntries = true)
    public UserDTO save(UserDTO dto) {
        // Validate unique username
        if (repository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + dto.getUsername());
        }

        UserDTO savedDto = super.save(dto);

        // Publish user created event
        kafkaTemplate.send("user-created", Map.of(
                "userId", savedDto.getId(),
                "username", savedDto.getUsername(),
                "timestamp", System.currentTimeMillis()
        ));

        return savedDto;
    }

    @Override
    @CacheEvict(value = {"userCache", "usersByUsername", "usersByEmail"}, allEntries = true)
    public UserDTO update(UserDTO dto, MappingOptions options) {
        UserDTO updatedDto = super.update(dto, options);

        // Publish user updated event
        kafkaTemplate.send("user-updated", Map.of(
                "userId", updatedDto.getId(),
                "timestamp", System.currentTimeMillis()
        ));

        return updatedDto;
    }

    @Override
    @CacheEvict(value = {"userCache", "usersByUsername", "usersByEmail"}, allEntries = true)
    public void deleteById(Long id) {
        super.deleteById(id);

        // Publish user deleted event
        kafkaTemplate.send("user-deleted", Map.of(
                "userId", id,
                "timestamp", System.currentTimeMillis()
        ));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserDTO registerUser(UserDTO userDTO) {
        // Validate user data
        validationService.validate(userDTO, OnCreate.class);

        // Check if username exists
        if (existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
        }

        // Create user
        UserDTO savedUser = save(userDTO);

        // Create verification token
        verificationTokenService.createToken(savedUser.getId(), VerificationToken.TokenType.EMAIL_VERIFICATION, 1440);

        // Publish event
        kafkaTemplate.send("user-registered", Map.of(
                "userId", savedUser.getId(),
                "username", savedUser.getUsername(),
                "timestamp", System.currentTimeMillis()
        ));

        return savedUser;
    }
}