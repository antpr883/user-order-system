package com.userorder.service.dto.mappers;

import com.userorder.persistance.model.Address;
import com.userorder.persistance.model.Contact;
import com.userorder.persistance.model.User;
import com.userorder.service.dto.AddressDTO;
import com.userorder.service.dto.ContactDTO;
import com.userorder.service.dto.UserDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Мапер для сутностей User та DTO з можливістю динамічного мапінгу
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
     */
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
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
     */
    public User processAddresses(User user, Set<AddressDTO> addressDtos, MappingOptions options) {
        if (options.getCollectionStrategy() == MappingOptions.CollectionHandlingStrategy.IGNORE || addressDtos == null) {
            return user;
        }

        // If strategy is REPLACE, remove all existing addresses
        if (options.getCollectionStrategy() == MappingOptions.CollectionHandlingStrategy.REPLACE) {
            Set<Address> existingAddresses = new HashSet<>(user.getAddresses());
            existingAddresses.forEach(user::removeAddress);
        }

        // Add or update addresses from DTOs
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
     * Legacy method for compatibility
     */
    public User processAddresses(User user, Set<AddressDTO> addressDtos) {
        return processAddresses(user, addressDtos, MappingOptions.builder()
                .collectionStrategy(MappingOptions.CollectionHandlingStrategy.MERGE)
                .build());
    }

    /**
     * Process contacts for a user
     */
    public User processContacts(User user, Set<ContactDTO> contactDtos, MappingOptions options) {
        if (options.getCollectionStrategy() == MappingOptions.CollectionHandlingStrategy.IGNORE || contactDtos == null) {
            return user;
        }

        // If strategy is REPLACE, remove all existing contacts
        if (options.getCollectionStrategy() == MappingOptions.CollectionHandlingStrategy.REPLACE) {
            Set<Contact> existingContacts = new HashSet<>(user.getContacts());
            existingContacts.forEach(user::removeContact);
        }

        // Add or update contacts from DTOs
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

    /**
     * Legacy method for compatibility
     */
    public User processContacts(User user, Set<ContactDTO> contactDtos) {
        return processContacts(user, contactDtos, MappingOptions.builder()
                .collectionStrategy(MappingOptions.CollectionHandlingStrategy.MERGE)
                .build());
    }

    /**
     * Update a user with relations
     */
    public User updateUserWithRelations(User user, UserDTO dto, MappingOptions options) {
        // Update simple user fields
        updateUserFromDto(dto, user);

        // Process related entities if present in DTO
        if (dto.getAddresses() != null) {
            processAddresses(user, dto.getAddresses(), options);
        }

        if (dto.getContacts() != null) {
            processContacts(user, dto.getContacts(), options);
        }

        return user;
    }
}