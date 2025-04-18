package com.userorder.service;

import java.util.List;
import java.util.Set;

/**
 * Base service interface that defines common CRUD operations with flexible mapping options
 *
 * @param <T> The DTO type
 */
public interface BaseService<T> {

    /**
     * Delete entity by ID
     * 
     * @param id Entity ID
     */
    void deleteById(Long id);

    /**
     * Find entity by ID with configurable options for audit information and included attributes
     * 
     * @param id Entity ID
     * @param withAudit Whether to include audit information (createdDate, modifiedDate, etc.)
     * @param attributes Set of attributes to include (like "addresses", "contacts", "roles.permissions")
     * @return DTO with requested configuration
     */
    T findById(Long id, boolean withAudit, Set<String> attributes);

    /**
     * Find entity by ID with default configuration (no audit, no additional attributes)
     * 
     * @param id Entity ID
     * @return DTO with basic fields
     */
    default T findById(Long id) {
        return findById(id, false, null);
    }
    
    /**
     * Find all entities with configurable options for audit information and included attributes
     * 
     * @param withAudit Whether to include audit information (createdDate, modifiedDate, etc.)
     * @param attributes Set of attributes to include (like "addresses", "contacts", "roles.permissions")
     * @return List of DTOs with requested configuration
     */
    List<T> findAll(boolean withAudit, Set<String> attributes);

    /**
     * Find all entities with default configuration (no audit, no additional attributes)
     * 
     * @return List of DTOs with basic fields
     */
    default List<T> findAll() {
        return findAll(false, null);
    }
    
}