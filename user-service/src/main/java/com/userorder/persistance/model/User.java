package com.userorder.persistance.model;

import com.userorder.persistance.model.common.PersistenceModel;
import com.userorder.persistance.utils.MappingAttribute;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@NamedEntityGraph(
    name = "User.complete",
    attributeNodes = {
        @NamedAttributeNode("addresses"),
        @NamedAttributeNode("contacts")
    }
)
public class User extends PersistenceModel {

    @EqualsAndHashCode.Include
    @NaturalId
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @ToString.Exclude
    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Builder.Default
    @Column(name = "login_attempts")
    private Integer loginAttempts = 0;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @ToString.Exclude
    @Builder.Default
    @MappingAttribute(
            path = "addresses",
            withSubAttributes = true,
            targetEntity = Address.class,
            description = "User addresses"
    )
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @MappingAttribute(
            path = "contacts",
            withSubAttributes = true,
            targetEntity = Contact.class,
            description = "User contacts"
    )
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Contact> contacts = new HashSet<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role_names", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_name")
    private Set<String> roleNames = new HashSet<>();

    public void addAddress(Address address) {
        if (address != null) {
            this.addresses.add(address);
            address.setUser(this);
        }
    }

    public void removeAddress(Address address) {
        if (address != null && this.addresses.contains(address)) {
            this.addresses.remove(address);
            address.setUser(null);
        }
    }

    public void addContact(Contact contact) {
        if (contact != null) {
            this.contacts.add(contact);
            contact.setUser(this);
        }
    }

    public void removeContact(Contact contact) {
        if (contact != null && this.contacts.contains(contact)) {
            this.contacts.remove(contact);
            contact.setUser(null);
        }
    }

    // Custom constructor with essential fields
    public User(String username, String password, String firstName, String lastName, LocalDate birthDay) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
    }
}