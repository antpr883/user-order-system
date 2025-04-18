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
 * DTO for User Preference entities
 * Always includes the user ID it belongs to
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPreferenceDTO extends BaseDTO {

    // The ID of the user these preferences belong to
    private Long userId;

    // Preference fields
    private String language;
    private String timezone;
    private Boolean notificationEnabled;
    private String theme;

    // Audit information as a nested object
    private AuditDTO audit;

}
