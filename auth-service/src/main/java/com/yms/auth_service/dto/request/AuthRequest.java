package com.yms.auth_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class AuthRequest.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
