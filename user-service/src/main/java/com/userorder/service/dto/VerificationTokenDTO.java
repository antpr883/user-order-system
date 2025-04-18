package com.userorder.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.userorder.persistance.model.VerificationToken;
import com.userorder.service.dto.base.AuditDTO;
import com.userorder.service.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * DTO for verification tokens
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationTokenDTO extends BaseDTO {
    private String token;
    private Long userId;
    private LocalDateTime expiryDate;
    private String tokenType;
    private Boolean isUsed;
    private AuditDTO audit;

    /**
     * Checks if the token is expired
     * @return true if token is expired
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
