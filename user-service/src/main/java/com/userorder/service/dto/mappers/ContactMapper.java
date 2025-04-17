package com.userorder.service.dto.mappers;

import com.userorder.persistance.model.Contact;
import com.userorder.persistance.model.ContactType;
import com.userorder.service.dto.ContactDTO;
import org.mapstruct.*;

import java.util.Set;

/**
 * Mapper for Contact entities and DTOs
 */
@Mapper(config = CommonMapperConfig.class, uses = {AuditMapper.class})
public interface ContactMapper {

    /**
     * Maps a Contact entity to a ContactDTO
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "contactType", expression = "java(contact.getContactType().name())")
    @Mapping(target = "audit", expression = "java(AuditMapper.mapAuditData(contact))")
    ContactDTO toDto(Contact contact);

    /**
     * Maps a Contact entity to a ContactDTO with dynamic attribute inclusion
     */
    @Mapping(target = "userId", source = "user.id",
            conditionExpression = "java(options.includesPath(\"user\"))")
    @Mapping(target = "audit", expression = "java(options.includeAudit() ? AuditMapper.mapAuditData(contact) : null)")
    ContactDTO toDtoWithOptions(Contact contact, @Context MappingOptions options);

    /**
     * Maps a ContactDTO to a Contact entity
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "contactType", expression = "java(ContactType.valueOf(dto.getContactType()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Contact toEntity(ContactDTO dto);

    /**
     * Updates a Contact entity from a ContactDTO
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "contactType", expression = "java(dto.getContactType() != null ? ContactType.valueOf(dto.getContactType()) : contact.getContactType())")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateContactFromDto(ContactDTO dto, @MappingTarget Contact contact);
}