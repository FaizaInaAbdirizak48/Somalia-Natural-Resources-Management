package com.snrms.backend.service;

import com.snrms.backend.dto.UserDTO;
import com.snrms.backend.dto.UserRequestDTO;
import com.snrms.backend.entity.User;
import com.snrms.backend.exception.DuplicateResourceException;
import com.snrms.backend.exception.ResourceInUseException;
import com.snrms.backend.exception.ResourceNotFoundException;
import com.snrms.backend.mapper.UserMapper;
import com.snrms.backend.repository.ReportRepository;
import com.snrms.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for managing user accounts (Users.jsx: Create / Read /
 * Update / Delete). Login lives separately in AuthService, since
 * authenticating an existing user is a different concern from managing
 * the roster of accounts.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserDTO getUserById(Long id) {
        return userMapper.toDTO(findUserOrThrow(id));
    }

    @Transactional
    public UserDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email '" + dto.getEmail() + "' is already registered.");
        }
        if (dto.getPasswordHash() == null || dto.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException("Password is required for new users.");
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

    @Transactional
    public UserDTO updateUser(Long id, UserRequestDTO dto) {
        User user = findUserOrThrow(id);

        boolean usernameChanged = !user.getUsername().equalsIgnoreCase(dto.getUsername());
        if (usernameChanged && userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already taken.");
        }

        boolean emailChanged = !user.getEmail().equalsIgnoreCase(dto.getEmail());
        if (emailChanged && userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email '" + dto.getEmail() + "' is already registered.");
        }

        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());

        // Matches Users.jsx: blank password on edit means "keep the old one".
        if (dto.getPasswordHash() != null && !dto.getPasswordHash().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(dto.getPasswordHash()));
        }

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserOrThrow(id);

        if (reportRepository.existsByGeneratedBy_UserID(id)) {
            throw new ResourceInUseException(
                    "Cannot delete '" + user.getFullName() + "': they have generated one or more reports.");
        }

        userRepository.delete(user);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
