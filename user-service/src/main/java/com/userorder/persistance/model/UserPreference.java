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
@Table(name = "user_preferences")
public class UserPreference extends PersistenceModel {

    @ToString.Exclude
    @MappingAttribute(
            path = "user",
            targetEntity = User.class,
            description = "User these preferences belong to"
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "language", length = 10)
    private String language = "en";

    @Column(name = "timezone", length = 50)
    private String timezone = "UTC";

    @Column(name = "notification_enabled")
    private Boolean notificationEnabled = true;

    @Column(name = "theme")
    private String theme = "light";
}