package com.userorder.service.dto;


import com.userorder.persistence.model.ContactType;
import com.userorder.service.dto.base.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Contact data transfer object")
public class ContactDTO extends BaseDTO {
    
    @NotNull(message = "Contact type is required")
    @Schema(description = "Type of contact", required = true)
    private ContactType contactType;
    
    @Pattern(regexp = "^\\+?[0-9 ()-]{8,20}$", message = "Phone number must be valid and between 8-20 digits")
    @Schema(description = "Phone number in international format")
    private String phoneNumber;
    
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Schema(description = "Email address")
    private String email;
    
    // Reference to parent only by ID to avoid circular references
    @Schema(description = "ID of the user this contact belongs to")
    private Long userId;
}