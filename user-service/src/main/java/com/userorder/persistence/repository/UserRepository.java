package com.userorder.persistence.repository;


import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.userorder.persistence.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseCustomJpaRepository<User, Long> , JpaSpecificationExecutor<User> {

     Optional<User> findById(Long id, EntityGraph entityGraph);
     
     List<User> findAll(EntityGraph entityGraph);

}