package com.userorder.persistance.repository;

import com.userorder.persistance.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends BaseCustomJpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByContactsEmail(String email);
}
