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
@Table(name = "addresses")
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

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @ToString.Exclude
    @MappingAttribute(
            path = "user",
            targetEntity = User.class,
            description = "User address belongs to"
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Constructor for common fields
    public Address(AddressType type, String street, String postZipCode, String province, String city, String country) {
        this.type = type;
        this.street = street;
        this.postZipCode = postZipCode;
        this.province = province;
        this.city = city;
        this.country = country;
    }

    public void setUser(User user) {
        this.user = user;
    }
}