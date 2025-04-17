package com.userorder.service.dto;

import com.userorder.service.dto.base.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for password change operations through secure channel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequestDTO implements DTO {
    private Long userId;

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}
