package com.userorder.service.impl;


import com.userorder.persistence.model.User;
import com.userorder.persistence.repository.UserRepository;
import com.userorder.service.UserService;
import com.userorder.service.dto.UserDTO;
import com.userorder.service.dto.mapper.UserMapper;
import com.userorder.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of UserService
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl
    extends AbstractBaseService<User, UserDTO, UserRepository, UserMapper>
    implements UserService {

    public UserServiceImpl(UserRepository repository,
                           UserMapper userMapper,
                           GraphBuilderMapperService graphBuilderService) {
        super(repository, userMapper, graphBuilderService);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findEntityById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public UserDTO save(UserDTO userDTO) {
        // Convert DTO to entity
        User user = mapper.toEntity(userDTO);
        
        // Save entity
        user = repository.save(user);
        
        // Return mapped entity as DTO
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {
        // Find existing entity
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        // Update entity with DTO, ignoring null values
        mapper.partialUpdate(user, userDTO);
        
        // Save updated entity
        user = repository.save(user);
        
        // Return mapped entity as DTO
        return mapper.toDto(user);
    }
}