package com.snrms.backend.controller;

import com.snrms.backend.dto.LoginDTO;
import com.snrms.backend.dto.LoginResponseDTO;
import com.snrms.backend.dto.RegisterDTO;
import com.snrms.backend.dto.UserDTO;
import com.snrms.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints. Note the path: the frontend's authService.js
 * calls POST /api/Users/login (nested under "Users", not "Auth") - so we
 * match that exactly here instead of using a separate /api/Auth path.
 */
@RestController
@RequestMapping("/api/Users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/Users/register
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO dto) {
        UserDTO created = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // POST /api/Users/login  -> matches authService.js's login() call exactly
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    // POST /api/Users/logout
    // Since we're using stateless JWTs (no server-side session), there's
    // nothing to invalidate here - the frontend just discards the token.
    // This endpoint exists so the frontend has something to call.
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    // GET /api/Users/profile -> returns the currently-authenticated user
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
        UserDTO profile = authService.getProfile(authentication.getName());
        return ResponseEntity.ok(profile);
    }
}
