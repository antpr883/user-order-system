package com.userorder.service.dto.mapper;


import com.userorder.persistence.model.Contact;
import com.userorder.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * MapStruct-based mapper for Contact entity
 */
@Mapper(componentModel = "spring", uses = {MapperUtils.class}, config = MapStructConfig.class)
public interface ContactMapper extends EntityMapper<Contact, ContactDTO> {

    @Override
    @Named("toDto")
    @Mapping(target = "userId", source = "user.id")
    ContactDTO toDto(Contact entity);

    @Override
    @Mapping(target = "user", ignore = true)
    Contact toEntity(ContactDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    void partialUpdate(@MappingTarget Contact entity, ContactDTO dto);

    /**
     * Maps contact with options to control property inclusion
     */
    @Override
    @Named("toDtoWithOptions")
    @Mapping(target = "userId", source = "user.id",
            conditionExpression = "java(options.includesPath(\"user\") || MapperUtils.hasAncestorOfType(entity, com.userorder.persistence.model.User.class))")
    ContactDTO toDtoWithOptions(Contact entity, @Context MappingOptions options);
    
    /**
     * Process BaseDTO fields based on options
     */
    @AfterMapping
    default void processBaseDtoFields(@MappingTarget ContactDTO dto, Contact entity, @Context MappingOptions options) {
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
    ContactDTO toSimpleDto(Contact contact);
}