package com.snrms.backend.config;

import com.snrms.backend.security.CustomUserDetailsService;
import com.snrms.backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * This is the central "rulebook" for who can access what.

 * Design decision (documented so it's not a silent surprise):
 * The frontend has public pages (Home, PublicProjects, PublicResearch,
 * PublicGISMap) that call the SAME "GET /api/Projects" etc. endpoints
 * used by the logged-in dashboard - and they do it without ever logging in.
 * So GET requests on Categories/Resources/Projects/Reports are public
 * (read-only, no sensitive data), while every write (POST/PUT/DELETE) and
 * anything under /api/Users requires a valid JWT.

 * IMPORTANT: for protected calls to work, the frontend must attach the
 * token it gets back from login as a header on every request:
 *   Authorization: Bearer <token>
 * (e.g. via an axios request interceptor in services/api.js). Until that's
 * wired up, write operations will correctly return 401/403.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt: the standard, battle-tested way to hash passwords.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // We're building a stateless REST API (no server-side sessions/cookies),
            // so CSRF protection - which exists to protect cookie-based sessions -
            // isn't needed here.
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Anyone can attempt to log in / register
                .requestMatchers("/api/Users/login", "/api/Users/register").permitAll()
                // Public read-only data for the public-facing pages
                .requestMatchers(HttpMethod.GET, "/api/Categories/**", "/api/Resources/**",
                        "/api/Projects/**", "/api/Reports/**").permitAll()
                // Everything else needs a valid JWT
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Vite's default dev server ports - add your production frontend URL here too.
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
