package com.userorder.persistance.model;

import com.userorder.persistance.model.common.PersistenceModel;
import com.userorder.persistance.utils.MappingAttribute;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact extends PersistenceModel {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactType contactType;

    @EqualsAndHashCode.Include
    @Column(length = 30)
    private String phoneNumber;

    @EqualsAndHashCode.Include
    @Column(length = 50)
    private String email;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @ToString.Exclude
    @MappingAttribute(
            path = "user",
            targetEntity = User.class,
            description = "User contact belongs to"
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Constructor for common fields
    public Contact(ContactType contactType, String phoneNumber, String email) {
        this.contactType = contactType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setUser(User user) {
        this.user = user;
    }
}