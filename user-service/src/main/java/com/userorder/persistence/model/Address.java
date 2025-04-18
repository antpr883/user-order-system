package com.userorder.persistence.model;

import com.userorder.persistence.model.base.PersistenceModel;
import com.userorder.persistence.utils.mapping.MappingAttribute;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address extends PersistenceModel {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType type;

    @Column(length = 150)
    private String street;

    @Column(length = 20)
    private String postZipCode;

    @Column(name = "province", length = 50)
    private String province;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "country", length = 50)
    private String country;


    @ToString.Exclude
    @MappingAttribute(
            path = "user",
            targetEntity = User.class,
            description = "User addresses"
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    
    public void addUser(User user) {
        if (user != null) {
            this.user = user;
            if (!user.getAddresses().contains(this)) {
                user.getAddresses().add(this);
            }
        }
    }

    public void removeUser() {
        if (this.user != null) {
            User tempUser = this.user;
            this.user = null;
            tempUser.getAddresses().remove(this);
        }
    }

    public Address(AddressType type, String street, String postZipCode, String province, String city, String country) {
        this.type = type;
        this.street = street;
        this.postZipCode = postZipCode;
        this.province = province;
        this.city = city;
        this.country = country;
    }
}