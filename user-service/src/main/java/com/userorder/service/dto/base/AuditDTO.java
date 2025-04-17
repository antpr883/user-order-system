package com.userorder.service.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * DTO for audit information - read-only
 * This data is managed by the system and should never be modified by clients
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditDTO implements Serializable {
    private String createdBy;
    private String modifiedBy;
    private String createdDate;
    private String modifiedDate;
}
