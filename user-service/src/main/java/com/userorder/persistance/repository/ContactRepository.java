package com.userorder.persistance.repository;

import com.userorder.persistance.model.Contact;
import com.userorder.persistance.model.ContactType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends BaseCustomJpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    List<Contact> findByUserId(Long userId);
    Optional<Contact> findByUserIdAndContactType(Long userId, ContactType contactType);
    Optional<Contact> findByPhoneNumber(String phoneNumber);
    Optional<Contact> findByEmail(String email);
    boolean existsByEmail(String email);
}
