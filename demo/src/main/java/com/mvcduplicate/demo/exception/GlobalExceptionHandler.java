package com.mvcduplicate.demo.exception;
// WHERE: Keep this handler under your application's base package.

import com.mvcduplicate.demo.dto.ApiResponse;
// Use your existing class for the response body.

import org.hibernate.exception.ConstraintViolationException;
// Read the database constraint name from Hibernate's exception.

import org.springframework.dao.DataIntegrityViolationException;
// Handle the Spring exception shown in your server log.

import org.springframework.http.HttpStatus;
// Provides HTTP status names such as CONFLICT.

import org.springframework.http.ResponseEntity;
// Controls the HTTP status and response body.

import org.springframework.web.bind.annotation.ExceptionHandler;
// Marks the method that handles a particular exception.

import org.springframework.web.bind.annotation.RestControllerAdvice;
// Registers shared exception handling for REST controllers.

@RestControllerAdvice
// WHAT: Register this class as a shared REST exception handler.
// HOW: Spring calls its handler methods when matching exceptions escape.

public class GlobalExceptionHandler {
// Translate database exceptions into HTTP responses.

    @ExceptionHandler(DataIntegrityViolationException.class)
    // Handle the database integrity exception from your registration request.

    public ResponseEntity<ApiResponse> handleDataIntegrity(
            DataIntegrityViolationException exception) {
        // Receive the exception and return an HTTP response.

        Throwable cause = exception;
        // Start examining the outer exception.

        while (cause != null) {
            // Continue while there is an exception to examine.

            if (cause instanceof ConstraintViolationException violation) {
                // Find Hibernate's constraint exception inside the cause chain.

                String constraintName = violation.getConstraintName();
                // Read the name of the database rule that failed.

                if ("ukr43af9ap4edm43mmtq01oddj6".equals(constraintName)
                        || "uk_users_username".equals(constraintName)) {
                    // Recognise your existing username constraint
                    // or the name declared in our updated User entity.

                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(new ApiResponse(
                                    false, "Username already exists"));
                    // Return 409 with a clear message.
                }
            }

            cause = cause.getCause();
            // Examine the underlying exception next.
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(
                        false, "Unable to complete registration"));
        // Return a general response for other integrity failures.
        // Do not expose internal database details.
    }
}