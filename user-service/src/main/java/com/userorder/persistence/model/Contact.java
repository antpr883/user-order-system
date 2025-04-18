package com.userorder.persistence.model;

import com.userorder.persistence.model.base.PersistenceModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contact extends PersistenceModel {

    @Enumerated(EnumType.STRING)
    @Column
    private ContactType contactType;

    @EqualsAndHashCode.Include
    @Column(unique = true, length = 30)
    private String phoneNumber;

    @EqualsAndHashCode.Include
    @Column(unique = true, length = 50)
    private String email;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    
    public void addUser(User user) {
        if (user != null) {
            this.user = user;
            if (!user.getContacts().contains(this)) {
                user.getContacts().add(this);
            }
        }
    }

    public void removeUser() {
        if (this.user != null) {
            User tempUser = this.user;
            this.user = null;
            tempUser.getContacts().remove(this);
        }
    }

    public Contact(ContactType contactType, String phoneNumber, String email){
        this.contactType = contactType;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}