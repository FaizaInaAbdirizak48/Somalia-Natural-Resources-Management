package com.snrms.backend.controller;

import com.snrms.backend.dto.UserDTO;
import com.snrms.backend.dto.UserRequestDTO;
import com.snrms.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Plain CRUD for user accounts - what Users.jsx's userService.js calls.
 * Login/register/logout live in AuthController, also under /api/Users,
 * on the more specific sub-paths (/login, /register) so there's no clash.
 */
@RestController
@RequestMapping("/api/Users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/Users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/Users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // POST /api/Users
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        UserDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/Users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // DELETE /api/Users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
