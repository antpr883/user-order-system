package com.userorder.persistence.repository;


import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.userorder.persistence.model.Contact;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ContactRepository extends BaseCustomJpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    
    /**
     * Find all contacts for a user by user ID
     * 
     * @param userId the ID of the user
     * @return list of contacts for the specified user
     */
    List<Contact> findByUserId(Long userId);
    
    /**
     * Find all contacts for a user by user ID with entity graph
     * 
     * @param userId the ID of the user
     * @param entityGraph the entity graph to apply
     * @return list of contacts for the specified user
     */
    List<Contact> findByUserId(Long userId, EntityGraph entityGraph);
}