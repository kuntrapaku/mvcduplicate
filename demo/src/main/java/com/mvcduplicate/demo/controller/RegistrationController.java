package com.mvcduplicate.demo.controller;
// WHAT: The package is the address of this class.
// WHERE: Controllers belong in your existing controller package.

import com.mvcduplicate.demo.dto.ApiResponse;
// Import your existing response class.
// It contains fields such as success and message.

import com.mvcduplicate.demo.model.User;
// Import the User class used to receive registration details.

import com.mvcduplicate.demo.service.AuthService;
// Import the service that handles registration decisions.

import org.springframework.http.HttpStatus;
// Import names for HTTP status codes, such as CREATED and CONFLICT.

import org.springframework.http.ResponseEntity;
// Import the class used to control the HTTP status and response body.

import org.springframework.web.bind.annotation.PostMapping;
// Import the annotation that maps HTTP POST requests to a method.

import org.springframework.web.bind.annotation.RequestBody;
// Import the annotation that reads JSON from the request body.

import org.springframework.web.bind.annotation.RequestMapping;
// Import the annotation that defines a shared URL path.

import org.springframework.web.bind.annotation.RestController;
// Import the annotation that registers a REST controller.

@RestController
// WHAT: Makes this class a Spring REST controller.
// HOW: Spring discovers it and registers its endpoint methods.
// Returned response objects are written to the HTTP response as JSON.

@RequestMapping("/auth")
// WHERE: Every endpoint in this controller starts with /auth.

public class RegistrationController {
// Receives registration requests and returns HTTP responses.

    private final AuthService authService;
    // Store the service dependency.
    // final prevents reassignment of this reference.

    public RegistrationController(AuthService authService) {
        // Spring supplies AuthService through this constructor.

        this.authService = authService;
        // Store the supplied service for use by the endpoint.
    }

    @PostMapping("/register")
    // Handle POST requests sent to /auth/register.

    public ResponseEntity<ApiResponse> registerUser(
            @RequestBody User user) {
        // Convert the incoming JSON into a User object.
        // Return an HTTP response containing your ApiResponse object.

        try {
            // Attempt registration through the service.

            String message = authService.registerUser(user);
            // Call the service and receive its success message.

            ApiResponse response = new ApiResponse(true, message);
            // Create a response body showing that registration succeeded.
            // Return only the message, not the User or password.

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
            // Return HTTP 201 Created with the response body.

        } catch (IllegalStateException exception) {
            // Handle the existing-username exception from AuthService.
            // This handles the service's username pre-check.

            ApiResponse response =
                    new ApiResponse(false, exception.getMessage());
            // Create a failure response using the service's message.

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(response);
            // Return HTTP 409 Conflict because the username is taken.
        }
    }
}