package com.userorder.service.dto.mappers;

import com.userorder.persistance.model.Address;
import com.userorder.persistance.model.AddressType;
import com.userorder.service.dto.AddressDTO;
import org.mapstruct.*;

import java.util.Set;

/**
 * Mapper for Address entities and DTOs
 */
@Mapper(config = CommonMapperConfig.class, uses = {AuditMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    /**
     * Maps an Address entity to an AddressDTO
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "type", expression = "java(address.getType().name())")
    @Mapping(target = "audit", expression = "java(AuditMapper.mapAuditData(address))")
    AddressDTO toDto(Address address);

    /**
     * Maps an Address entity to an AddressDTO with dynamic attribute inclusion
     */
    @Mapping(target = "userId", source = "user.id",
            conditionExpression = "java(options.includesPath(\"user\"))")
    @Mapping(target = "audit", expression = "java(options.includeAudit() ? AuditMapper.mapAuditData(address) : null)")
    AddressDTO toDtoWithOptions(Address address, @Context MappingOptions options);

    /**
     * Maps an AddressDTO to an Address entity
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", expression = "java(AddressType.valueOf(dto.getType()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Address toEntity(AddressDTO dto);

    /**
     * Updates an Address entity from an AddressDTO
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", expression = "java(dto.getType() != null ? AddressType.valueOf(dto.getType()) : address.getType())")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDto(AddressDTO dto, @MappingTarget Address address);
}