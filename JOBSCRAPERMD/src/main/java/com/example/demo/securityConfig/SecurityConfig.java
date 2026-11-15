package com.example.demo.securityConfig;




import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.repository.UserRepository;

@Configuration
public class SecurityConfig {


@Bean
public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(
        List.of("http://localhost:5173")
    );

    configuration.setAllowedMethods(
        List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
    );

    configuration.setAllowedHeaders(
        List.of("*")
    );

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return  source;
}

	
	
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository) {

        return email -> {
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                        new org.springframework.security.core.userdetails
                                .UsernameNotFoundException("User not found")
                    );

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        };
    }


@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {

    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())

        .authorizeHttpRequests(auth -> auth

            // Public endpoints
            .requestMatchers("/api/auth/register").permitAll()
            .requestMatchers("/api/jobs", "/api/jobs/**").permitAll()
            .requestMatchers("/actuator/health").permitAll()

            // Temporarily public for scraper testing
            .requestMatchers("/api/admin/scrape").permitAll()
            .requestMatchers("/api/admin/scraper-logs").permitAll()
            .requestMatchers("/api/admin/scraper-logs/**").permitAll()

            // Other endpoints require authentication
            .anyRequest().authenticated()
        );

    return http.build();
}
}