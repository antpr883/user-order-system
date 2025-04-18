package com.userorder.service.dto;


import com.userorder.persistence.model.AddressType;
import com.userorder.service.dto.base.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Address data transfer object")
public class AddressDTO extends BaseDTO {
    
    @NotNull(message = "Address type is required")
    @Schema(description = "Type of address", required = true)
    private AddressType type;
    
    @NotBlank(message = "Street is required")
    @Size(max = 100, message = "Street cannot exceed 100 characters")
    @Schema(description = "Street address", required = true)
    private String street;
    
    @NotBlank(message = "Post/Zip code is required")
    @Size(max = 20, message = "Post/Zip code cannot exceed 20 characters")
    @Schema(description = "Postal or ZIP code", required = true)
    private String postZipCode;
    
    @Size(max = 50, message = "Province cannot exceed 50 characters")
    @Schema(description = "Province or state")
    private String province;
    
    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    @Schema(description = "City", required = true)
    private String city;
    
    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    @Schema(description = "Country", required = true)
    private String country;

    // Reference to parent only by ID to avoid circular references
    @Schema(description = "ID of the user this address belongs to")
    private Long userId;
}