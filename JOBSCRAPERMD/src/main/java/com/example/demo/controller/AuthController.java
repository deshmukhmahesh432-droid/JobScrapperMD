package com.example.demo.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() ->
                    new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
        ));
    }
    
    
    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User user) {

        if (user.getName() == null ||
            user.getEmail() == null ||
            user.getPassword() == null ||
            user.getPassword().length() < 8) {

            return ResponseEntity.badRequest()
                .body(Map.of(
                    "message",
                    "Name, email and password of at least 8 characters are required"
                ));
        }

        if (userRepository.existsByEmail(user.getEmail())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Email already registered"));
        }

        user.setPassword(
            passwordEncoder.encode(user.getPassword())
        );

        // Public registration must never assign ADMIN.
        user.setRole("USER");

        User saved = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "email", saved.getEmail(),
                "message", "Registration successful"
            ));
    }
}
