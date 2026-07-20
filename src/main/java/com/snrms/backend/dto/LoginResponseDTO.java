package com.snrms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthContext.jsx comment says it verbatim:
 * "Expected response format from the backend: { userID, fullName, username, role }"
 * We add "token" on top - the frontend currently ignores extra fields,
 * but it's there for when you wire up ProtectedRoute to check a real JWT
 * instead of just an in-memory user object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private Long userID;
    private String fullName;
    private String username;
    private String role;
    private String token;
}
