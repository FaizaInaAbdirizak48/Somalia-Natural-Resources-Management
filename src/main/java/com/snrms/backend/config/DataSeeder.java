package com.snrms.backend.config;

import com.snrms.backend.entity.User;
import com.snrms.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * There's a chicken-and-egg problem: /api/Users/login requires an existing
 * user, but the frontend has no "sign up" screen - only a login form.
 * This runs once every time the app starts, and creates one default admin
 * account IF the users table is completely empty, so you have something
 * to log in with on day one.

 * CHANGE THIS PASSWORD (or delete this class) before deploying anywhere
 * that isn't your own laptop.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setUsername("admin");
            admin.setEmail("admin@nrm.gov.so");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
            admin.setRole("Admin");
            admin.setActive(true);
            userRepository.save(admin);

            System.out.println("=================================================");
            System.out.println(" No users found - created a default admin account:");
            System.out.println("   username: admin");
            System.out.println("   password: Admin@123");
            System.out.println(" Please log in and change this password immediately.");
            System.out.println("=================================================");
        }
    }
}
