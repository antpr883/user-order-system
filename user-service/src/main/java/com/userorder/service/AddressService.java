package com.userorder.service;


import com.userorder.persistence.model.Address;
import com.userorder.service.dto.AddressDTO;

import java.util.Optional;

/**
 * Service interface for Address entity operations
 */
public interface AddressService extends BaseAssociationService<AddressDTO> , BaseService<AddressDTO> {
    
    /**
     * Find an address entity by ID without mapping to DTO
     */
    Optional<Address> findEntityById(Long id);
    
    /**
     * Save a new or update an existing address
     */
    AddressDTO save(AddressDTO addressDTO);
    
    /**
     * Partially update an address
     */
    AddressDTO update(Long id, AddressDTO addressDTO);
    
    /**
     * Find all addresses for a specific user with configurable options
     * 
     * @param userId the ID of the user
     * @param withAudit whether to include audit information
     * @return list of address DTOs for the specified user
     */
    // List<AddressDTO> findAllByUserId(Long userId, boolean withAudit);

    
}