package com.userorder.service;

import com.userorder.persistance.model.UserPreference;
import com.userorder.service.dto.UserPreferenceDTO;

public interface UserPreferenceService extends BaseService<UserPreference, UserPreferenceDTO> {
    UserPreferenceDTO findByUserId(Long userId);
    UserPreferenceDTO createDefaultPreferences(Long userId);
    UserPreferenceDTO updateUserPreferences(Long userId, UserPreferenceDTO preferencesDTO);
}