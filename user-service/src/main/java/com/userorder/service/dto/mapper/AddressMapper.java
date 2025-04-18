package com.userorder.service.dto.mapper;


import com.userorder.persistence.model.Address;
import com.userorder.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * MapStruct-based mapper for Address entity
 */
@Mapper(componentModel = "spring", uses = {MapperUtils.class}, config = MapStructConfig.class)
public interface AddressMapper extends EntityMapper<Address, AddressDTO> {

    @Override
    @Named("toDto")
    @Mapping(target = "userId", source = "user.id")
    AddressDTO toDto(Address entity);

    @Override
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    void partialUpdate(@MappingTarget Address entity, AddressDTO dto);

    /**
     * Maps address with options to control property inclusion
     */
    @Override
    @Named("toDtoWithOptions")
    @Mapping(target = "userId", source = "user.id",
            conditionExpression = "java(options.includesPath(\"user\") || MapperUtils.hasAncestorOfType(entity, com.userorder.persistence.model.User.class))")
    AddressDTO toDtoWithOptions(Address entity, @Context MappingOptions options);
    
    /**
     * Process BaseDTO fields based on options
     */
    @AfterMapping
    default void processBaseDtoFields(@MappingTarget AddressDTO dto, Address entity, @Context MappingOptions options) {
        // Handle BaseDTO fields - only include audit information if requested
        if (!options.includeAudit()) {
            // If audit information is not requested, clear all audit fields except ID
            Long id = dto.getId(); // Save the ID
            dto.setCreatedDate(null);
            dto.setModifiedDate(null);
            dto.setCreatedBy(null);
            dto.setModifiedBy(null);
            dto.setId(id); // Restore the ID
        }
    }

    /**
     * Simplified DTO with minimal fields for list views
     */
    @Named("toSimpleDto")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    AddressDTO toSimpleDto(Address address);
}