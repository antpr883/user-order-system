package com.userorder.service;



import com.userorder.persistence.model.User;
import com.userorder.service.dto.UserDTO;

import java.util.Optional;

/**
 * Service interface for User entity operations
 */
public interface UserService extends BaseService<UserDTO> {
    
    /**
     * Find a user entity by ID without mapping to DTO
     */
    Optional<User> findEntityById(Long id);
    
    /**
     * Save a new or update an existing user
     */
    UserDTO save(UserDTO userDTO);
    
    /**
     * Partially update a user
     */
    UserDTO update(Long id, UserDTO userDTO);
}