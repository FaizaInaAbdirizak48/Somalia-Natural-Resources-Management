package com.snrms.backend.service;

import com.snrms.backend.dto.LoginDTO;
import com.snrms.backend.dto.LoginResponseDTO;
import com.snrms.backend.dto.RegisterDTO;
import com.snrms.backend.dto.UserDTO;
import com.snrms.backend.entity.User;
import com.snrms.backend.exception.DuplicateResourceException;
import com.snrms.backend.mapper.UserMapper;
import com.snrms.backend.repository.UserRepository;
import com.snrms.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Handles the "Authentication" part of Step 8: register, login, logout,
 * and reading the current profile.
 *
 * Login flow (matches authService.js / AuthContext.jsx exactly):
 *   1. Frontend POSTs { username, email, password } to /api/Users/login
 *      (it sends the same typed value in both username and email fields,
 *      because it doesn't know which one the person typed).
 *   2. We try to find a User by username first, then by email.
 *   3. We compare the raw password against the stored BCrypt hash.
 *   4. On success we return { userID, fullName, username, role, token } -
 *      exactly the shape AuthContext.jsx expects (plus a bonus JWT token).
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserDTO register(RegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email '" + dto.getEmail() + "' is already registered.");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPasswordHash()));
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    public LoginResponseDTO login(LoginDTO dto) {
        User user = findByUsernameOrEmail(dto.getUsername(), dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password."));

        if (!user.isActive()) {
            throw new BadCredentialsException("This account has been deactivated.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new LoginResponseDTO(user.getUserID(), user.getFullName(), user.getUsername(), user.getRole(), token);
    }

    public UserDTO getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User not found."));
        return userMapper.toDTO(user);
    }

    private Optional<User> findByUsernameOrEmail(String username, String email) {
        if (username != null && !username.isBlank()) {
            Optional<User> byUsername = userRepository.findByUsername(username);
            if (byUsername.isPresent()) return byUsername;
        }
        if (email != null && !email.isBlank()) {
            return userRepository.findByEmail(email);
        }
        return Optional.empty();
    }
}
