package com.userorder.persistance.repository;

import com.userorder.persistance.model.Address;
import com.userorder.persistance.model.AddressType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends BaseCustomJpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    List<Address> findByUserId(Long userId);
    Optional<Address> findByUserIdAndIsDefault(Long userId, boolean isDefault);
    List<Address> findByUserIdAndType(Long userId, AddressType type);
}
