package com.userorder.persistence.model;


import com.userorder.persistence.model.base.PersistenceModel;
import com.userorder.persistence.utils.mapping.MappingAttribute;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User extends PersistenceModel {

    @ToString.Exclude
    @Column(length = 16)
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "birthday")
    private LocalDate birthDay;

    @ToString.Exclude
    @Builder.Default
    @MappingAttribute(
            path = "addresses",
            targetEntity = Address.class,
            description = "User addresses"
    )
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @MappingAttribute(
            path = "contacts",
            targetEntity = Contact.class,
            description = "User contacts"
    )
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Contact> contacts = new HashSet<>();

    public void setAddress(Address address) {
        if (address != null) {
            this.addresses.add(address);
            address.setUser(this);
        }
    }
    
    public void addAddress(Address address) {
        if (address != null) {
            this.addresses.add(address);
            address.setUser(this);
        }
    }

    public void removeAddress(Address address){
        this.addresses.remove(address);
        address.setUser(null);
    }

    public void setContact(Contact contact) {
        if (contact != null) {
            this.contacts.add(contact);
            contact.setUser(this);
        }
    }
    
    public void addContact(Contact contact) {
        if (contact != null) {
            this.contacts.add(contact);
            contact.setUser(this);
        }
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setUser(null);
    }

    public User(String password, String firstName, String lastName, LocalDate birthDay) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
    }
}
