package com.userorder.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;

import com.userorder.persistence.model.Contact;
import com.userorder.persistence.model.User;
import com.userorder.persistence.repository.ContactRepository;
import com.userorder.persistence.repository.UserRepository;
import com.userorder.service.ContactService;
import com.userorder.service.dto.ContactDTO;
import com.userorder.service.dto.mapper.ContactMapper;
import com.userorder.service.dto.mapper.MappingOptions;
import com.userorder.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ContactService
 */
@Slf4j
@Service
@Transactional
public class ContactServiceImpl 
    extends AbstractBaseService<Contact, ContactDTO, ContactRepository, ContactMapper>
    implements ContactService {

    private final UserRepository userRepository;

    public ContactServiceImpl(ContactRepository repository,
                             ContactMapper contactMapper,
                             GraphBuilderMapperService graphBuilderService,
                             UserRepository userRepository) {
        super(repository, contactMapper, graphBuilderService);
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Find the contact to delete
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));

        // Remove the contact from its user (if associated with one)
        // This maintains the bidirectional relationship and prevents cascade deletion
        if (contact.getUser() != null) {
            contact.removeUser();
        }

        // Now safely delete the contact
        repository.delete(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contact> findEntityById(Long id) {
        return repository.findById(id);
    }

    /**
     * Creates a default entity graph for contact entities
     * Used when no specific attributes are requested
     */
    @Override
    protected EntityGraph createDefaultEntityGraph() {
        // For a contact, we typically don't need to fetch anything by default
        // Return a minimal graph for performance
        return super.createDefaultEntityGraph();
    }

    @Override
    @Transactional
    public ContactDTO save(ContactDTO contactDTO) {
        // Convert DTO to entity
        Contact contact = mapper.toEntity(contactDTO);
        
        // Handle user association if userId is provided
        if (contactDTO.getUserId() != null) {
            User user = userRepository.findById(contactDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + contactDTO.getUserId()));

            // Set up bidirectional relationship
            user.addContact(contact);
        }

        // Save entity
        contact = repository.save(contact);
        
        // Return mapped entity as DTO
        return mapper.toDto(contact);
    }

    @Override
    @Transactional
    public ContactDTO update(Long id, ContactDTO contactDTO) {
        // Find existing entity
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
        
        // Check if userId is being changed
        Long newuserId = contactDTO.getUserId();
        if (newuserId != null && (contact.getUser() == null || !newuserId.equals(contact.getUser().getId()))) {
            // Handle user reassignment
            handleUserReassignment(contact, newuserId);
        }

        // Update entity with DTO, ignoring null values
        mapper.partialUpdate(contact, contactDTO);
        
        // Save updated entity
        contact = repository.save(contact);
        
        // Return mapped entity as DTO
        return mapper.toDto(contact);
    }
    
    /**
     * Handles reassignment of a contact from one user to another
     *
     * @param contact The contact to reassign
     * @param newuserId The ID of the new user to assign the contact to
     */
    private void handleUserReassignment(Contact contact, Long newuserId) {
        // First, remove the contact from its current user (if any)
        if (contact.getUser() != null) {
            contact.removeUser();
        }

        // Then, find the new user and add the contact to it
        User newUser = userRepository.findById(newuserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + newuserId));

        // Add the contact to the new user (sets up both sides of the relationship)
        newUser.addContact(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDTO> findByUserId(Long userId, boolean withAudit) {

        List<Contact> contacts = repository.findByUserId(userId);
        
        // Map to DTOs with appropriate options
        MappingOptions options = MappingOptions.builder()
                .withAudit(withAudit)
                .entityClass(Contact.class)
                .build();
        
        return mapper.toDtoListWithOptions(contacts, options);
    }
    
}