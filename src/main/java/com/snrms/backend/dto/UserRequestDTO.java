package com.snrms.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Matches the exact object Users.jsx builds in handleSubmit():
 *   { fullName, username, email, passwordHash, role, isActive, userID }
 *
 * On UPDATE, the frontend may send an empty/blank passwordHash
 * (meaning "don't change the password") - so passwordHash is NOT
 * @NotBlank here. The service layer decides whether to re-hash it.
 */
@Data
public class UserRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    // Optional on update, required on create (checked manually in the service).
    private String passwordHash;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "Admin|Manager", message = "Role must be either 'Admin' or 'Manager'")
    private String role;

    @com.fasterxml.jackson.annotation.JsonProperty("isActive")
    private boolean isActive = true;
}
