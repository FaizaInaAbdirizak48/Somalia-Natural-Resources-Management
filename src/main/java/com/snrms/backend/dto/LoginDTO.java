package com.snrms.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * authService.js sends BOTH "username" and "email" set to the same value
 * (whatever the person typed in the login box), plus "password".
 * We accept both fields but only require that AT LEAST one identifier
 * is usable - see AuthService for the actual lookup logic.
 */
@Data
public class LoginDTO {

    private String username;

    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
