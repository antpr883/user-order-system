package com.userorder.service.impl;


import com.userorder.persistence.model.Address;
import com.userorder.persistence.model.User;
import com.userorder.persistence.repository.AddressRepository;
import com.userorder.persistence.repository.UserRepository;
import com.userorder.service.AddressService;
import com.userorder.service.dto.AddressDTO;
import com.userorder.service.dto.mapper.AddressMapper;
import com.userorder.service.dto.mapper.MappingOptions;
import com.userorder.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of AddressService
 */
@Slf4j
@Service
@Transactional
public class AddressServiceImpl
        extends AbstractBaseService<Address, AddressDTO, AddressRepository, AddressMapper>
        implements AddressService {

    /**
     * Inject UserRepository for address reassignment
     */
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository repository,
                              AddressMapper addressMapper,
                              GraphBuilderMapperService graphBuilderService,
                              UserRepository userRepository) {
        super(repository, addressMapper, graphBuilderService);
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Find the address to delete
        Address address = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + id));
        address.removeUser();
        // Now safely delete the address
        repository.delete(address);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findEntityById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public AddressDTO save(AddressDTO addressDTO) {
        // Convert DTO to entity
        Address address = mapper.toEntity(addressDTO);

        // Handle user association if userId is provided
        if (addressDTO.getUserId() != null) {
            User user = userRepository.findById(addressDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + addressDTO.getUserId()));
            user.addAddress(address);
        }

        // Save entity
        address = repository.save(address);

        // Return mapped entity as DTO
        return mapper.toDto(address);
    }

    @Override
    @Transactional
    public AddressDTO update(Long id, AddressDTO addressDTO) {
        // Find existing entity
        Address address = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + id));

        // Check if userId is being changed
        Long newUserId = addressDTO.getUserId();
        if (newUserId != null && (address.getUser() == null || !newUserId.equals(address.getUser().getId()))) {
            // Handle user reassignment
            handleUserReassignment(address, newUserId);
        }

        // Update entity with DTO, ignoring null values
        mapper.partialUpdate(address, addressDTO);

        // Save updated entity
        address = repository.save(address);

        // Return mapped entity as DTO
        return mapper.toDto(address);
    }

    /**
     * Handles reassigning an address from one user to another
     * Maintains bidirectional relationship integrity
     */
    private void handleUserReassignment(Address address, Long newUserId) {
        // First, remove the address from its current user (if any)
        if (address.getUser() != null) {
            address.removeUser();
        }

        // Then, find the new user and add the address to it
        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + newUserId));

        // Add the address to the new user
        newUser.addAddress(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> findByUserId(Long userId, boolean withAudit) {

        // Fetch entities with graph
        List<Address> addresses = repository.findByUserId(userId);

        // Map to DTOs with appropriate options
        MappingOptions options = MappingOptions.builder()
                .withAudit(withAudit)
                .build();

        return mapper.toDtoListWithOptions(addresses, options);
    }

}