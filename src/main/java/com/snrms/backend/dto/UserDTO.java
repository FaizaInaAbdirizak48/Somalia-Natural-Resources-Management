package com.snrms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What we send BACK to the frontend for a user.
 * Deliberately has NO password field - never expose password hashes,
 * not even hashed ones, in an API response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userID;
    private String fullName;
    private String username;
    private String email;
    private String role;
    private boolean isActive;
}
