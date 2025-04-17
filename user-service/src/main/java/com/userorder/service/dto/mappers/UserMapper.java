package com.userorder.service.dto.mappers;

import com.userorder.persistance.model.Address;
import com.userorder.persistance.model.Contact;
import com.userorder.persistance.model.User;
import com.userorder.service.dto.AddressDTO;
import com.userorder.service.dto.ContactDTO;
import com.userorder.service.dto.UserDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Mapper for User entities and DTOs with dynamic mapping capabilities
 */
@Mapper(config = CommonMapperConfig.class, uses = {AddressMapper.class, ContactMapper.class, AuditMapper.class})
public abstract class UserMapper {

    @Autowired
    protected AddressMapper addressMapper;

    @Autowired
    protected ContactMapper contactMapper;

    /**
     * Maps a User entity to a UserDTO
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "audit", expression = "java(AuditMapper.mapAuditData(user))")
    public abstract UserDTO toDto(User user);

    /**
     * Maps a User entity to a UserDTO with dynamic attribute inclusion
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "audit", expression = "java(options.includeAudit() ? AuditMapper.mapAuditData(user) : null)")
    @Mapping(target = "addresses", source = "addresses", conditionExpression = "java(options.includesPath(\"addresses\"))")
    @Mapping(target = "contacts", source = "contacts", conditionExpression = "java(options.includesPath(\"contacts\"))")
    public abstract UserDTO toDtoWithOptions(User user, @Context MappingOptions options);

    /**
     * Maps a UserDTO to a User entity
     * Ignores relationships and system-managed fields
     */
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    public abstract User toEntity(UserDTO dto);

    /**
     * Updates a User entity from a UserDTO
     * Ignores relationships and system-managed fields
     */
    @Mapping(target = "username", ignore = true) // Username cannot be changed
    @Mapping(target = "password", ignore = true) // Password has special handling
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateUserFromDto(UserDTO dto, @MappingTarget User user);

    /**
     * Process addresses for a user
     * Creates Address entities from DTOs and associates them with the user
     */
    public User processAddresses(User user, Set<AddressDTO> addressDtos) {
        if (addressDtos == null || addressDtos.isEmpty()) {
            return user;
        }

        addressDtos.forEach(dto -> {
            if (dto.getId() != null) {
                user.getAddresses().stream()
                        .filter(a -> a.getId().equals(dto.getId()))
                        .findFirst()
                        .ifPresent(address -> addressMapper.updateAddressFromDto(dto, address));
            } else {
                Address address = addressMapper.toEntity(dto);
                user.addAddress(address);
            }
        });

        return user;
    }

    /**
     * Process contacts for a user
     * Creates Contact entities from DTOs and associates them with the user
     */
    public User processContacts(User user, Set<ContactDTO> contactDtos) {
        if (contactDtos == null || contactDtos.isEmpty()) {
            return user;
        }

        contactDtos.forEach(dto -> {
            if (dto.getId() != null) {
                user.getContacts().stream()
                        .filter(c -> c.getId().equals(dto.getId()))
                        .findFirst()
                        .ifPresent(contact -> contactMapper.updateContactFromDto(dto, contact));
            } else {
                Contact contact = contactMapper.toEntity(dto);
                user.addContact(contact);
            }
        });

        return user;
    }
}