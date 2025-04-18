package com.userorder.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.userorder.persistence.model.base.PersistenceModel;
import com.userorder.persistence.repository.BaseCustomJpaRepository;
import com.userorder.service.BaseService;
import com.userorder.service.dto.mapper.EntityMapper;
import com.userorder.service.dto.mapper.MappingOptions;
import com.userorder.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Abstract base service implementation that provides common CRUD operations with flexible mapping
 *
 * @param <E> Entity type
 * @param <D> DTO type
 * @param <R> Repository type
 * @param <M> Mapper type
 */
@Transactional
@RequiredArgsConstructor
public abstract class AbstractBaseService<
        E extends PersistenceModel,
        D,
        R extends BaseCustomJpaRepository<E, Long>,
        M extends EntityMapper<E, D>> implements BaseService<D> {

    protected final R repository;
    protected final M mapper;
    protected final GraphBuilderMapperService graphBuilderService;
    protected final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    protected AbstractBaseService(R repository, M mapper, GraphBuilderMapperService graphBuilderService) {
        this.repository = repository;
        this.mapper = mapper;
        this.graphBuilderService = graphBuilderService;

        // Extract the entity class type using reflection
        this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public D findById(Long id, boolean withAudit, Set<String> attributes) {
        // Create an appropriate entity graph based on attributes
        EntityGraph graph;
        E entity;
        
        if (attributes == null || attributes.isEmpty()) {
            entity = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        } else {
            entity = repository.findByIdWithAttributes(id, attributes)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        }

        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .withAudit(withAudit)
                .entityClass(entityClass)
                .build();

        return mapper.toDtoWithOptions(entity, options);
    }
    
    /**
     * Creates a default entity graph that's lightweight but effective
     * for most common query scenarios
     */
    protected EntityGraph createDefaultEntityGraph() {
        return createSummaryLevelGraph();
    }
    
    /**
     * Creates an entity graph optimized for summary level mapping
     * - Includes just enough to get IDs and counts in a single query
     * - Designed to minimize database roundtrips
     */
    protected EntityGraph createSummaryLevelGraph() {
        DynamicEntityGraph.Builder builder = DynamicEntityGraph.fetching();
        
        // Analyze the entity class and add paths for all @OneToMany and @ManyToMany relations
        // This ensures we can get IDs and counts without additional queries
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(OneToMany.class) || 
                field.isAnnotationPresent(ManyToMany.class) ||
                field.isAnnotationPresent(ManyToOne.class)) {
                builder.addPath(field.getName());
            }
        }
        
        return builder.build();
    }

    /**
     * Helper method to create entity graph based on attribute set
     */
    protected EntityGraph createEntityGraph(Set<String> attributeSet) {
        // Get graph just for requested attributes
        return graphBuilderService.getGraphWithAttributes(entityClass, attributeSet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> findAll(boolean withAudit, Set<String> attributes) {
        List<E> entities;
        
        if (attributes == null || attributes.isEmpty()) {
            // No attributes specified, use default find method without graph
            Iterable<E> entitiesIterable = repository.findAll();
            entities = new ArrayList<>();
            entitiesIterable.forEach(entities::add);
        } else {
            // Generate targeted graph based on requested attributes
            EntityGraph graph = createEntityGraph(attributes);
            
            // Fetch entities with graph
            Iterable<E> entitiesIterable = repository.findAll(graph);
            entities = new ArrayList<>();
            entitiesIterable.forEach(entities::add);
        }

        // Map to DTOs with appropriate options
        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .withAudit(withAudit)
                .entityClass(entityClass) // Pass entity class for dynamic collection detection
                .build();

        return mapper.toDtoListWithOptions(entities, options);
    }
}