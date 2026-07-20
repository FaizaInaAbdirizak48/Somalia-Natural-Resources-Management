package com.snrms.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a row in the "users" table.
 * Matches exactly what Users.jsx reads/writes: fullName, username, email,
 * role ("Admin" | "Manager"), isActive.
 *
 * NOTE: the frontend calls the password field "passwordHash" even when creating
 * a user (it sends the raw password under that name). We accept that name here
 * and hash it ourselves in the service layer before saving - the frontend never
 * needs to know what a bcrypt hash looks like.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    // Stores the BCrypt-hashed password. Never returned to the frontend.
    @Column(nullable = false)
    private String passwordHash;

    // Kept as a simple String ("Admin" / "Manager") instead of a separate
    // Role table, because that is exactly what the frontend's <select> sends.
    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean isActive = true;
}
