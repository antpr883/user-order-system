package com.userorder.service.impl;

import com.userorder.persistance.model.User;
import com.userorder.persistance.model.UserPreference;
import com.userorder.persistance.repository.UserPreferenceRepository;
import com.userorder.persistance.repository.UserRepository;
import com.userorder.persistance.utils.GraphBuilderMappingService;
import com.userorder.service.UserPreferenceService;
import com.userorder.service.dto.UserPreferenceDTO;
import com.userorder.service.dto.mappers.MappingOptions;
import com.userorder.service.dto.mappers.UserPreferenceMapper;
import com.userorder.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserPreferenceServiceImpl extends AbstractBaseService<UserPreference, UserPreferenceDTO, UserPreferenceRepository> implements UserPreferenceService {

    private final UserPreferenceMapper preferenceMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserPreferenceServiceImpl(
            UserPreferenceRepository repository,
            GraphBuilderMappingService graphBuilderService,
            EntityManager entityManager,
            UserPreferenceMapper preferenceMapper,
            UserRepository userRepository) {
        super(repository, graphBuilderService, entityManager, UserPreference.class);
        this.preferenceMapper = preferenceMapper;
        this.userRepository = userRepository;
    }

    @Override
    protected Object getMapper() {
        return preferenceMapper;
    }

    @Override
    protected UserPreferenceDTO toDto(UserPreference entity, MappingOptions options) {
        return preferenceMapper.toDtoWithOptions(entity, options);
    }

    @Override
    protected UserPreference toEntity(UserPreferenceDTO dto) {
        return preferenceMapper.toEntity(dto);
    }

    @Override
    protected void updateEntityFromDto(UserPreferenceDTO dto, UserPreference entity, MappingOptions options) {
        // Only update fields that are not null in the DTO
        if (dto.getLanguage() != null) {
            entity.setLanguage(dto.getLanguage());
        }
        if (dto.getTimezone() != null) {
            entity.setTimezone(dto.getTimezone());
        }
        if (dto.getNotificationEnabled() != null) {
            entity.setNotificationEnabled(dto.getNotificationEnabled());
        }
        if (dto.getTheme() != null) {
            entity.setTheme(dto.getTheme());
        }
    }

    @Override
    @Cacheable(value = "userPreferencesByUserId", key = "#userId")
    @Transactional(readOnly = true)
    public UserPreferenceDTO findByUserId(Long userId) {
        log.debug("Finding user preferences for user ID: {}", userId);

        UserPreference preferences = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User preferences not found for user ID: " + userId));

        return toDto(preferences, MappingOptions.builder().includeAudit(true).build());
    }

    @Override
    @Transactional
    public UserPreferenceDTO createDefaultPreferences(Long userId) {
        log.debug("Creating default preferences for user ID: {}", userId);

        // Check if preferences already exist
        if (repository.findByUserId(userId).isPresent()) {
            throw new IllegalStateException("User preferences already exist for user ID: " + userId);
        }

        // Get the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Create default preferences
        UserPreference preferences = UserPreference.builder()
                .user(user)
                .language("en")
                .timezone("UTC")
                .notificationEnabled(true)
                .theme("light")
                .build();

        preferences = repository.save(preferences);
        return toDto(preferences, MappingOptions.builder().build());
    }

    @Override
    @CacheEvict(value = "userPreferencesByUserId", key = "#userId")
    @Transactional
    public UserPreferenceDTO updateUserPreferences(Long userId, UserPreferenceDTO preferencesDTO) {
        log.debug("Updating preferences for user ID: {}", userId);

        UserPreference preferences = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User preferences not found for user ID: " + userId));

        // Update preferences from DTO
        updateEntityFromDto(preferencesDTO, preferences, MappingOptions.builder().build());

        preferences = repository.save(preferences);
        return toDto(preferences, MappingOptions.builder().includeAudit(true).build());
    }
}