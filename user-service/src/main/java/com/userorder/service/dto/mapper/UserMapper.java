package com.userorder.service.dto.mapper;


import com.userorder.persistence.model.User;
import com.userorder.service.dto.UserDTO;
import org.mapstruct.*;

import java.util.Collections;

/**
 * MapStruct-based mapper for User entity
 */
@Mapper(componentModel = "spring", 
        uses = {AddressMapper.class, ContactMapper.class, MapperUtils.class},
        config = MapStructConfig.class)
public interface UserMapper extends EntityMapper<User, UserDTO> {

    @Override
    @Named("toDto")
    @Mapping(target = "addressIds", expression = "java(MapperUtils.extractIds(user.getAddresses()))")
    @Mapping(target = "contactIds", expression = "java(MapperUtils.extractIds(user.getContacts()))")
    @Mapping(target = "addresses", source = "addresses", qualifiedByName = "toDto")
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "toDto")
    @Mapping(target = "addressCount", expression = "java(MapperUtils.safeCount(user.getAddresses()))")
    @Mapping(target = "contactCount", expression = "java(MapperUtils.safeCount(user.getContacts()))")
    UserDTO toDto(User user);

    @Override
    @Named("toDtoWithOptions")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "addresses", source = "addresses", qualifiedByName = "toDtoWithOptions", 
            conditionExpression = "java(options.includesPath(\"addresses\"))")
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "toDtoWithOptions", 
            conditionExpression = "java(options.includesPath(\"contacts\"))")
    @Mapping(target = "password", source = "password",
            conditionExpression = "java(options.includes(\"password\"))")
    UserDTO toDtoWithOptions(User user, @Context MappingOptions options);

    /**
     * Special method to process DTO based on mapping options
     */
    @AfterMapping
    default void processFieldsBasedOnOptions(@MappingTarget UserDTO dto, User user, @Context MappingOptions options) {
        // Handle audit fields
        if (!options.includeAudit()) {
            // If audit information is disabled, clear all audit fields except ID
            Long id = dto.getId(); // Save the ID
            dto.setCreatedDate(null);
            dto.setModifiedDate(null);
            dto.setCreatedBy(null);
            dto.setModifiedBy(null);
            dto.setId(id); // Restore the ID
        }
        
        // Handle password field - only include for certain conditions
        // For security, always exclude password unless explicitly requested
        if (!options.includes("password")) {
            dto.setPassword(null);
        }
        
        // Handle collection entities based on attributes
        // Addresses
        if (options.includesPath("addresses")) {
            if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
                // Already mapped in the main mapper method
                dto.setAddressIds(MapperUtils.extractIds(user.getAddresses()));
                dto.setAddressCount(user.getAddresses().size());
            }
        } else {
            dto.setAddresses(Collections.emptySet());
            dto.setAddressIds(Collections.emptySet());
            dto.setAddressCount(0);
        }
        
        // Contacts
        if (options.includesPath("contacts")) {
            if (user.getContacts() != null && !user.getContacts().isEmpty()) {
                dto.setContactIds(MapperUtils.extractIds(user.getContacts()));
                dto.setContactCount(user.getContacts().size());
            }
        } else {
            dto.setContacts(Collections.emptySet());
            dto.setContactIds(Collections.emptySet());
            dto.setContactCount(0);
        }

    }

    @Override
    @Named("toEntity")
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    User toEntity(UserDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    void partialUpdate(@MappingTarget User entity, UserDTO dto);

    /**
     * After mapping, handle collections and bidirectional relationships
     */
    @AfterMapping
    default void handleCollectionsForEntity(@MappingTarget User user, UserDTO dto) {
        // Implementation would handle bidirectional relationships and collections
        // This would be where we'd set up relationships with addresses, contacts, and roles
    }
}