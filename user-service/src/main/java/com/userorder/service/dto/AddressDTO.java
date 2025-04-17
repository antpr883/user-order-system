package com.userorder.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.userorder.service.dto.base.AuditDTO;
import com.userorder.service.dto.base.BaseDTO;
import com.userorder.service.dto.base.OnCreate;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for addresses
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO extends BaseDTO {
    private Long userId;

    @NotBlank(groups = OnCreate.class)
    private String type;

    @NotBlank(groups = OnCreate.class)
    private String street;

    private String postZipCode;
    private String province;

    @NotBlank(groups = OnCreate.class)
    private String city;

    @NotBlank(groups = OnCreate.class)
    private String country;

    private Boolean isDefault;

    private AuditDTO audit;
}