package com.userorder.service;

import com.userorder.persistance.model.User;
import com.userorder.service.dto.PasswordChangeRequestDTO;
import com.userorder.service.dto.UserDTO;

import java.util.Optional;
import java.util.Set;

/**
 * Сервіс для роботи з користувачами.
 */
public interface UserService extends BaseService<UserDTO> {

    /**
     * Знаходить користувача за ім'ям користувача
     */
    UserDTO findByUsername(String username, boolean includeAudit, Set<String> attributes);

    /**
     * Знаходить користувача за email
     */
    UserDTO findByEmail(String email, boolean includeAudit, Set<String> attributes);

    /**
     * Перевіряє чи існує користувач з таким ім'ям
     */
    boolean existsByUsername(String username);

    /**
     * Змінює пароль користувача за токеном
     */
    UserDTO changePassword(PasswordChangeRequestDTO passwordChangeDto);
}