package com.userorder.persistance.repository;

import com.userorder.persistance.model.UserPreference;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends BaseCustomJpaRepository<UserPreference, Long> {

    Optional<UserPreference> findByUserId(Long userId);
}