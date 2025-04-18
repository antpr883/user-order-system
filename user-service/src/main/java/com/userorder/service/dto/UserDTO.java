package com.userorder.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.userorder.persistance.model.UserStatus;
import com.userorder.service.dto.base.AuditDTO;
import com.userorder.service.dto.base.BaseDTO;
import com.userorder.service.dto.base.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Unified DTO for User entity with support for different detail levels
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseDTO {
    // Basic information
    @NotBlank(groups = OnCreate.class)
    private String username;

    // Password is write-only, never returned in responses
    @NotBlank(groups = OnCreate.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    private UserStatus status;
    private LocalDateTime lastLoginDate;
    private Integer loginAttempts;
    private String profilePictureUrl;
    private Set<String> roleNames;

    // Complete related entities for detailed views
    @Valid
    @Builder.Default
    private Set<AddressDTO> addresses = new HashSet<>();
    @Valid
    @Builder.Default
    private Set<ContactDTO> contacts = new HashSet<>();

    // Audit information as a nested object
    private AuditDTO audit;

    /**
     * Helper method to check if this is a new user (for creation)
     */
    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }
}