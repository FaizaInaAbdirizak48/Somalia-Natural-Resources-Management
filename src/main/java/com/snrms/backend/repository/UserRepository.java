package com.snrms.backend.repository;

import com.snrms.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JpaRepository already gives us findAll(), findById(), save(), deleteById()
 * for free. We only need to add the extra lookup methods Spring Data JPA
 * can auto-generate just from the method name.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
