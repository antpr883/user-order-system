package com.userorder.persistence.repository;

import com.userorder.persistence.model.Address;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AddressRepository extends BaseCustomJpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    
    /**
     * Find all addresses for a user by user ID
     * 
     * @param userId the ID of the user
     * @return list of addresses for the specified user
     */
    List<Address> findByUserId(Long userId);

}