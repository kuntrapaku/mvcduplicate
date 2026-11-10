package com.mvcduplicate.demo.service;
// WHAT: The package is the address of this class.
// WHERE: This class belongs in your existing service package.

import com.mvcduplicate.demo.model.User;
// Import the entity that represents a registered user.

import com.mvcduplicate.demo.repository.UserRepository;
// Import the repository used to search and save users.

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// Import the BCrypt implementation used to hash passwords.

import org.springframework.security.crypto.password.PasswordEncoder;
// Import the interface that defines password-encoding operations.

import org.springframework.stereotype.Service;
// Import the annotation that registers this class as a Spring service.

@Service
// WHAT: Marks this class as a Spring-managed service.
// WHERE: We put registration business logic in this service.
// HOW: Spring discovers it through component scanning.

public class AuthService {
// Handles the decisions needed to register a user.

    private final UserRepository userRepository;
    // Store the repository dependency.
    // final prevents this reference from being reassigned.

    private final PasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();
    // Create a BCrypt password encoder.
    // The reference uses the interface; the object is its implementation.
    // We will later learn how to inject this dependency through Spring.

    public AuthService(UserRepository userRepository) {
        // Spring calls this constructor and supplies the repository.

        this.userRepository = userRepository;
        // Store the supplied repository in this service object.
    }

    public String registerUser(User user) {
        // Receive the user passed by the controller.
        // Return a success message if registration succeeds.

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // Read the username from the User object.
            // Search the database using the repository.
            // isPresent() is true when a matching user exists.

            throw new IllegalStateException("Username already exists");
            // Stop registration because the username is already taken.
            // We will map this failure to an HTTP response in the controller.
        }

        String encodedPassword =
                passwordEncoder.encode(user.getPassword());
        // Read the supplied password and hash it using BCrypt.
        // Keep the resulting hash in a local variable.

        user.setPassword(encodedPassword);
        // Replace the plain password in this object with its hash.
        // This ensures the repository receives the encoded value.

        userRepository.save(user);
        // Ask JPA to persist the user.
        // Database constraints still decide whether the write is allowed.

        return "User registered successfully";
        // Return this message only after save() completes successfully.
    }
}