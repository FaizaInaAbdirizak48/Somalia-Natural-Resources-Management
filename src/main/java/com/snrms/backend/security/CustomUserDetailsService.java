package com.snrms.backend.security;

import com.snrms.backend.entity.User;
import com.snrms.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Spring Security doesn't know anything about our "User" entity out of the
 * box - it only understands its own UserDetails interface. This class is
 * the translator: "given a username, here's how to look up a matching User
 * in the database and describe it in terms Spring Security understands."
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .disabled(!user.isActive())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())))
                .build();
    }
}
