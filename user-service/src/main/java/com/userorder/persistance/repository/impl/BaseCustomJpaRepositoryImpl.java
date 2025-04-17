package com.userorder.persistance.repository.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphSimpleJpaRepository;
import com.userorder.persistance.repository.BaseCustomJpaRepository;
import com.userorder.persistance.utils.GraphBuilderMappingService;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BaseCustomJpaRepositoryImpl<T, ID extends Serializable> extends EntityGraphSimpleJpaRepository<T, ID> implements BaseCustomJpaRepository<T, ID> {

    private final JpaEntityInformation<T, ID> entityInformation;
    private final EntityManager entityManager;
    private final Class<T> domainClass;
    private final GraphBuilderMappingService graphBuilderService;

    public BaseCustomJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation,
                                       EntityManager entityManager,
                                       GraphBuilderMappingService graphBuilderService) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
        this.domainClass = entityInformation.getJavaType();
        this.graphBuilderService = graphBuilderService;
    }

    @Override
    public List<T> findAllByIdWithAttributes(Collection<ID> ids, Collection<String> attributes) {
        EntityGraph entityGraph = graphBuilderService.getGraphWithAttributes(domainClass, attributes);
        return findAllById(ids, entityGraph);
    }

    @Override
    public Optional<T> findByIdWithAttributes(ID id, Collection<String> attributes) {
        EntityGraph entityGraph = graphBuilderService.getGraphWithAttributes(domainClass, attributes);
        return findById(id, entityGraph);
    }

    @Override
    public List<T> findAllWithAttributes(Specification<T> spec, Collection<String> attributes) {
        EntityGraph entityGraph = graphBuilderService.getGraphWithAttributes(domainClass, attributes);
        return findAll(spec, entityGraph);
    }

    @Override
    public Page<T> findPageWithAttributes(Specification<T> spec, Pageable pageable, Collection<String> attributes) {
        EntityGraph entityGraph = graphBuilderService.getGraphWithAttributes(domainClass, attributes);
        return findAll(spec, pageable, entityGraph);
    }
}