package com.userorder.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.userorder.persistance.model.common.PersistenceModel;
import com.userorder.persistance.repository.BaseCustomJpaRepository;
import com.userorder.persistance.utils.GraphBuilderMappingService;
import com.userorder.service.BaseService;
import com.userorder.service.dto.base.BaseDTO;
import com.userorder.service.dto.mappers.EntityMapper;
import com.userorder.service.dto.mappers.MappingOptions;
import com.userorder.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Абстрактна реалізація базового сервісу з повною CRUD функціональністю.
 *
 * @param <E> Тип сутності
 * @param <D> Тип DTO
 * @param <R> Тип репозиторію
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
public abstract class AbstractBaseService<E extends PersistenceModel,
        D extends BaseDTO,
        R extends BaseCustomJpaRepository<E, Long>,
        M extends EntityMapper<E, D>>
        implements BaseService<D> {

    protected final R repository;
    protected final M mapper;
    protected final GraphBuilderMappingService graphBuilderService;
    protected final EntityManager entityManager;
    protected final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    protected AbstractBaseService(R repository, M mapper, GraphBuilderMappingService graphBuilderService ,EntityManager entityManager) {
        this.repository = repository;
        this.mapper = mapper;
        this.graphBuilderService = graphBuilderService;
        this.entityManager = entityManager;

        // Extract the entity class type using reflection
        this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    @Transactional(readOnly = true)
    public D findById(Long id, boolean includeAudit, Set<String> attributes) {
        log.debug("Finding entity by ID {} with attributes: {}, includeAudit: {}", id, attributes, includeAudit);

        E entity;
        if (attributes == null || attributes.isEmpty()) {
            entity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(entityClass.getSimpleName() + " not found with id: " + id));
        } else {
            entity = repository.findByIdWithAttributes(id, attributes)
                    .orElseThrow(() -> new ResourceNotFoundException(entityClass.getSimpleName() + " not found with id: " + id));
        }

        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .includeAudit(includeAudit)
                .build();

        return mapper.toDtoWithOptions(entity, options);
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> findAll(boolean includeAudit, Set<String> attributes) {
        log.debug("Finding all entities with attributes: {}, includeAudit: {}", attributes, includeAudit);

        List<E> entities;
        if (attributes == null || attributes.isEmpty()) {
            entities = repository.findAll();
        } else {
            EntityGraph entityGraph = graphBuilderService.getGraphWithAttributes(entityClass, attributes);
            entities = repository.findAll(entityGraph);

        }

        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .includeAudit(includeAudit)
                .build();

        return entities.stream()
                .map(entity -> mapper.toDtoWithOptions(entity, options))
                .collect(Collectors.toList());
    }

    @Override
    public D save(D dto) {
        log.debug("Saving new entity: {}", dto);

        E entity = mapper.toEntity(dto);
        entity = repository.save(entity);

        return mapper.toDtoWithOptions(entity, MappingOptions.builder().build());
    }

    @Override
    public D update(D dto, MappingOptions options) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("ID must be provided for update operation");
        }

        log.debug("Updating entity with ID: {}", dto.getId());

        E entity = repository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(entityClass.getSimpleName() + " not found with id: " + dto.getId()));

        mapper.partialUpdate(entity ,dto);
        entity = repository.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting entity with ID: {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(entityClass.getSimpleName() + " not found with id: " + id);
        }

        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<D> findEntityById(Long id) {
        return repository.findById(id);
    }

    /**
     * Знаходить сутності за специфікацією з пагінацією та динамічними атрибутами
     */
    @Transactional(readOnly = true)
    public Page<D> findAll(Specification<E> spec, Pageable pageable, boolean includeAudit, Set<String> attributes) {
        log.debug("Finding entities by spec with pageable and attributes: {}", attributes);

        Page<E> entityPage;
        if (attributes == null || attributes.isEmpty()) {
            entityPage = repository.findAll(spec, pageable);
        } else {
            entityPage = repository.findPageWithAttributes(spec, pageable, attributes);
        }

        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .includeAudit(includeAudit)
                .build();

        return entityPage.map(entity -> toDto(entity, options));
    }
}