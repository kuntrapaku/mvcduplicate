package com.mvcduplicate.demo.service;
// WHERE: This test class belongs under src/test/java.

import com.mvcduplicate.demo.model.User;
// Import the user entity used by our registration service.

import com.mvcduplicate.demo.repository.UserRepository;
// Import the repository interface that we will mock.

import org.junit.jupiter.api.Test;
// Mark methods that JUnit should execute as tests.

import org.mockito.ArgumentCaptor;
// Capture the object passed to the repository's save method.

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// Check whether the saved password hash matches the original password.

import java.util.Optional;
// Represent a user found or a user missing from the repository.

import static org.junit.jupiter.api.Assertions.*;
// Import methods that check expected results.

import static org.mockito.Mockito.*;
// Import methods that create, configure, and verify mocks.

class AuthServiceTest {
// Contains unit tests for your existing AuthService.

    @Test
        // Run this method as a test.

    void existingUsernameShouldNotBeSaved() {
        // Check that an existing username is rejected.

        UserRepository repository = mock(UserRepository.class);
        // ARRANGE: Create a fake repository without a real database.

        AuthService service = new AuthService(repository);
        // Create the real service using the fake repository.

        User existingUser =
                new User("ravi", "stored-password-hash", "Ravi Kumar");
        // Represent an existing user.
        // The password is a placeholder because this path never checks it.

        User incomingUser =
                new User("ravi", "ExamplePassword123!", "Ravi Kumar");
        // Represent another registration with the same username.

        when(repository.findByUsername("ravi"))
                .thenReturn(Optional.of(existingUser));
        // Make the repository report that the username exists.

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.registerUser(incomingUser));
        // ACT: Attempt registration.
        // ASSERT: Check that it throws the expected exception.

        assertEquals("Username already exists", exception.getMessage());
        // Check the exception message.

        verify(repository, never()).save(any(User.class));
        // Check that the rejected request never calls save().
    }

    @Test
        // Run this method as a separate test.

    void newUsernameShouldBeSavedWithHashedPassword() {
        // Check successful registration and password hashing.

        UserRepository repository = mock(UserRepository.class);
        // ARRANGE: Create a fresh mock for this test.

        AuthService service = new AuthService(repository);
        // Create the real service with the mock repository.

        String rawPassword = "ExamplePassword123!";
        // Keep the original password so we can verify the saved hash.

        User incomingUser =
                new User("ravi", rawPassword, "Ravi Kumar");
        // Create the user who wants to register.

        when(repository.findByUsername("ravi"))
                .thenReturn(Optional.empty());
        // Make the repository report that the username is available.

        String message = service.registerUser(incomingUser);
        // ACT: Run the registration method.
        // The service hashes the password and calls save().

        assertEquals("User registered successfully", message);
        // ASSERT: Check the returned success message.

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        // Create a captor to inspect the User passed to save().

        verify(repository).save(captor.capture());
        // Check that save() was called exactly once.
        // Capture the User supplied in that call.

        User savedUser = captor.getValue();
        // Read the captured User object.
        // This is the object passed to the mock, not a database record.

        assertEquals("ravi", savedUser.getUsername());
        // Check that the correct username was passed for saving.

        assertEquals("Ravi Kumar", savedUser.getFullname());
        // Check that the full name was preserved.

        assertNotNull(savedUser.getPassword());
        // Check that a password value was supplied.

        assertNotEquals(rawPassword, savedUser.getPassword());
        // Check that the supplied value is not the original plain password.

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // Create an encoder to verify the password hash.

        assertTrue(encoder.matches(rawPassword, savedUser.getPassword()));
        // Check that the stored hash correctly matches the original password.
        // This is stronger than checking only its prefix.
    }
}