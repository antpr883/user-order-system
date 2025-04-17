package com.userorder.persistance.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseCustomJpaRepository<T, ID extends Serializable> extends EntityGraphJpaRepository<T, ID> {

    /**
     * Finds entities by IDs with dynamic entity graph loading based on the provided attributes.
     *
     * @param ids List of entity IDs
     * @param attributes Collection of attributes to fetch
     * @return List of entities with the specified attributes loaded
     */
    List<T> findAllByIdWithAttributes(Collection<ID> ids, Collection<String> attributes);

    /**
     * Finds a single entity by ID with dynamic entity graph loading based on the provided attributes.
     *
     * @param id Entity ID
     * @param attributes Collection of attributes to fetch
     * @return Optional containing the entity with the specified attributes loaded
     */
    Optional<T> findByIdWithAttributes(ID id, Collection<String> attributes);

    /**
     * Finds all entities matching the specification with dynamic entity graph loading.
     *
     * @param spec Specification defining the search criteria
     * @param attributes Collection of attributes to fetch
     * @return List of matching entities with the specified attributes loaded
     */
    List<T> findAllWithAttributes(Specification<T> spec, Collection<String> attributes);

    /**
     * Finds a page of entities matching the specification with dynamic entity graph loading.
     *
     * @param spec Specification defining the search criteria
     * @param pageable Pagination information
     * @param attributes Collection of attributes to fetch
     * @return Page of matching entities with the specified attributes loaded
     */
    Page<T> findPageWithAttributes(Specification<T> spec, Pageable pageable, Collection<String> attributes);
}