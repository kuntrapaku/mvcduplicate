package com.mvcduplicate.demo.exception;
// WHERE: This test belongs under src/test/java.

import com.mvcduplicate.demo.dto.ApiResponse;
// Import our response body class.

import org.hibernate.exception.ConstraintViolationException;
// Import the Hibernate exception that provides the constraint name.

import org.junit.jupiter.api.Test;
// Import the annotation that marks a test method.

import org.springframework.dao.DataIntegrityViolationException;
// Import the Spring database exception our handler receives.

import org.springframework.http.HttpStatus;
// Import HTTP status names.

import org.springframework.http.ResponseEntity;
// Import the handler's response type.

import static org.junit.jupiter.api.Assertions.*;
// Import methods that check whether results are correct.

import static org.mockito.Mockito.*;
// Import methods that create and configure fake objects.

class GlobalExceptionHandlerTest {
// Group the tests for our exception handler.

    @Test
        // Tell JUnit to run this method as a test.

    void duplicateUsernameShouldReturn409() {
        // Test that a username constraint failure produces 409.

        ConstraintViolationException databaseCause =
                mock(ConstraintViolationException.class);
        // ARRANGE: Create a fake Hibernate exception.
        // We do not need PostgreSQL for this test.

        when(databaseCause.getConstraintName())
                .thenReturn("ukr43af9ap4edm43mmtq01oddj6");
        // Make the fake exception return your username constraint name.

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException(
                        "Insert failed", databaseCause);
        // Put the database cause inside a Spring exception.
        // This creates the exception chain our handler expects.

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        // Create the real handler we are testing.

        ResponseEntity<ApiResponse> response =
                handler.handleDataIntegrity(exception);
        // ACT: Call the handler with the prepared exception.

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        // ASSERT: Check that the returned status is 409.

        ApiResponse body = response.getBody();
        // Read the response body.

        assertNotNull(body);
        // Check that the body exists.

        assertFalse(body.isSuccess());
        // Check that registration is reported as unsuccessful.

        assertEquals("Username already exists", body.getMessage());
        // Check that the message is correct.
    }
    void otherConstraintShouldReturn500() {
        // Test a database constraint that is not the username constraint.

        ConstraintViolationException databaseCause =
                mock(ConstraintViolationException.class);
        // ARRANGE: Create a fake Hibernate exception.
        // No real database is used.

        when(databaseCause.getConstraintName())
                .thenReturn("some_other_constraint");
        // Make the fake exception return an unrecognised constraint name.

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException(
                        "Insert failed", databaseCause);
        // Wrap the database cause in the Spring exception our handler receives.

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        // Create the real handler being tested.

        ResponseEntity<ApiResponse> response =
                handler.handleDataIntegrity(exception);
        // ACT: Call the handler with this exception.

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode());
        // ASSERT: Check that the fallback status is 500.

        ApiResponse body = response.getBody();
        // Read the response body.

        assertNotNull(body);
        // Check that the response body exists.

        assertFalse(body.isSuccess());
        // Check that the response reports failure.

        assertEquals(
                "Unable to complete registration",
                body.getMessage());
        // Check that the response contains our general failure message.
    }
}