package com.userorder.service.dto.mappers;

import org.mapstruct.*;

import java.util.List;
import java.util.Set;

/**
 * Generic mapper interface with common mapping methods.
 *
 * @param <E> Entity type
 * @param <D> DTO type
 */
@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface GenericMapper<E, D> {

    D toDto(E entity);

    List<D> toDtoList(List<E> entities);

    Set<D> toDtoSet(Set<E> entities);

    E toEntity(D dto);

    List<E> toEntityList(List<D> dtos);

    Set<E> toEntitySet(Set<D> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(D dto, @MappingTarget E entity);
}
