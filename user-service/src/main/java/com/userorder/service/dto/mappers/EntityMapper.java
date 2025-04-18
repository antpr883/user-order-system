package com.userorder.service.dto.mappers;

import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enhanced entity mapper interface with support for flexible mapping options
 *
 * @param <E> Entity type
 * @param <D> DTO type
 */
public interface EntityMapper<E, D>  {
    /**
     * Maps entity to DTO
     */
    @Named("toDto")
    D toDto(E entity);

    /**
     * Maps entity to DTO with specified mapping options
     */
    @Named("toDtoWithOptions")
    D toDtoWithOptions(E entity, @Context MappingOptions options);

    /**
     * Maps entity list to DTO list
     */
    default List<D> toDtoList(List<E> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Maps entity list to DTO list with options
     */
    @Named("toDtoListWithOptions")
    default List<D> toDtoListWithOptions(List<E> entities, @Context MappingOptions options) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(entity -> toDtoWithOptions(entity, options))
                .collect(Collectors.toList());
    }


    /**
     * Maps entity set to DTO set
     */
    default Set<D> toDtoSet(Set<E> entities) {
        if (entities == null) return Collections.emptySet();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    /**
     * Maps entity set to DTO set with options
     */
    @Named("toDtoSetWithOptions")
    default Set<D> toDtoSetWithOptions(Set<E> entities, @Context MappingOptions options) {
        if (entities == null) return Collections.emptySet();
        return entities.stream()
                .map(entity -> toDtoWithOptions(entity, options))
                .collect(Collectors.toSet());
    }



    /**
     * Maps DTO to entity
     */
    @Named("toEntity")
    E toEntity(D dto);

    /**
     * Maps DTO list to entity list
     */
    default List<E> toEntityList(List<D> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Maps DTO set to entity set
     */
    default Set<E> toEntitySet(Set<D> dtos) {
        if (dtos == null) return Collections.emptySet();
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    /**
     * Updates entity from DTO, ignoring null values
     */
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget E entity, D dto);
}
