package com.userorder.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.userorder.service.dto.base.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Unified DTO for User entity that can be used for all purposes
 * Supports different mapping levels through selective field population
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {
    
    // Setting for write-only password field (never returned in responses)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Password (write-only)", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;
    
    @Schema(description = "First name", example = "John", required = true)
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;
    
    @Schema(description = "Last name", example = "Doe", required = true)
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;
    
    @Schema(description = "Birth date", example = "1990-01-01", required = true)
    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDay;
    
    // Summary counts of related entities (used for SUMMARY level)
    private Integer addressCount;
    private Integer contactCount;
    private Integer roleCount;
    
    // Simple collections with IDs only (used for SUMMARY level)
    @Builder.Default
    private Set<Long> addressIds = new HashSet<>();
    @Builder.Default
    private Set<Long> contactIds = new HashSet<>();
    @Builder.Default
    private Set<Long> roleIds = new HashSet<>();
    
    // Full collections with nested DTOs (used for COMPLETE level)
    @Builder.Default
    private Set<AddressDTO> addresses = new HashSet<>();
    @Builder.Default
    private Set<ContactDTO> contacts = new HashSet<>();

}