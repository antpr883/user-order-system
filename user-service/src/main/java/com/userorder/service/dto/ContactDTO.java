package com.userorder.service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.userorder.service.dto.base.AuditDTO;
import com.userorder.service.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Contact DTO containing contact information.
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactDTO extends BaseDTO {
    // The ID of the user this contact belongs to
    private Long userId;

    // Contact fields
    private String contactType;
    private String phoneNumber;
    private String email;
    private Boolean isPrimary;
    private Boolean isVerified;

    // Audit information as a nested object
    private AuditDTO audit;
}