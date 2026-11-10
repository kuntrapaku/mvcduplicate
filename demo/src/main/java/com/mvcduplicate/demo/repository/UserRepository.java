package com.mvcduplicate.demo.repository;
// WHAT: The package is the address of this interface.
// WHERE: It belongs in your existing repository package.

import com.mvcduplicate.demo.model.User;
// Import the User entity that this repository saves and retrieves.

import org.springframework.data.jpa.repository.JpaRepository;
// Import the Spring Data interface that provides database operations.

import java.util.List;
// Used when a query can return multiple users.

import java.util.Optional;
// Used when a query may return one user or no user.

public interface UserRepository extends JpaRepository<User, Long> {
// WHAT: A repository interface provides access to stored users.
// HOW: Spring Data creates an implementation at runtime.
// User means this repository manages User entities.
// Long means the User entity's primary-key type is Long.

    Optional<User> findByUsername(String username);
    // WHAT: Find a user whose username matches the supplied value.
    // WHERE: AuthService calls this during registration.
    // HOW: Spring Data derives the query from the method name.
    // Found → return an Optional containing the User.
    // Not found → return Optional.empty().
    // The unique database constraint ensures at most one match.

    List<User> findByUsernameContainingIgnoreCase(String username);
    // WHAT: Find users whose usernames contain the supplied text.
    // HOW: Ignore uppercase/lowercase differences when searching.
    // Example: searching "rav" can match "Ravi" and "ravikumar".
    // No matches → return an empty list.
    // Keep this existing method for your user-search functionality.
}