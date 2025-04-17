package com.userorder.persistance.repository;

import com.userorder.persistance.model.User;

import java.util.Optional;

public interface UserRepository extends BaseCustomJpaRepository<User, Long>{
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByContactsEmail(String email);
}
