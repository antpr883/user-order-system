package com.userorder.service;

import com.userorder.persistence.model.Contact;
import com.userorder.service.dto.ContactDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Contact entity operations
 */
public interface ContactService extends BaseAssociationService<ContactDTO>, BaseService<ContactDTO> {

    /**
     * Find a contact entity by ID without mapping to DTO
     */
    Optional<Contact> findEntityById(Long id);

    /**
     * Save a new or update an existing contact
     */
    ContactDTO save(ContactDTO contactDTO);

    /**
     * Partially update a contact
     */
    ContactDTO update(Long id, ContactDTO contactDTO);

    /**
     * Find all contacts for a specific user with configurable options
     *
     * @param userId  the ID of the user
     * @param withAudit whether to include audit information
     * @return list of contact DTOs for the specified user
     */
    List<ContactDTO> findByUserId(Long userId, boolean withAudit);

}