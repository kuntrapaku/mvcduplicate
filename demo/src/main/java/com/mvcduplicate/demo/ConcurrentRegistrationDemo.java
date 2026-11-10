package com.mvcduplicate.demo;
// Place this programme in your existing test package.

import java.net.URI;
// Represents the registration endpoint address.

import java.net.http.HttpClient;
// Sends HTTP requests using Java's built-in HTTP client.

import java.net.http.HttpRequest;
// Represents an outgoing HTTP request.

import java.net.http.HttpResponse;
// Represents the response received from the server.

import java.time.Duration;
// Specifies a request timeout.

import java.util.concurrent.CompletableFuture;
// Represents a result that will become available later.

public class ConcurrentRegistrationDemo {
// A standalone programme for testing overlapping registrations.

    public static void main(String[] args) {
        // Run this method while your Spring Boot application is running.

        HttpClient client = HttpClient.newHttpClient();
        // Create one client that can send both requests.

        String username = "concurrent_" + System.nanoTime();
        // Generate a fresh test username for this run.
        // Both requests below will use this same username.

        String json = """
                {
                  "username": "%s",
                  "password": "ExamplePassword123!",
                  "fullname": "Concurrent Test"
                }
                """.formatted(username);
        // Create the JSON request body.
        // %s is replaced with our generated username.

        HttpRequest request = HttpRequest.newBuilder()
                // Start building an HTTP request.

                .uri(URI.create("http://localhost:8080/auth/register"))
                // Use your application's actual port.
                // Change 8081 if your application uses another port.

                .timeout(Duration.ofSeconds(15))
                // Fail the request if it takes too long.

                .header("Content-Type", "application/json")
                // Tell Spring that the body contains JSON.

                .POST(HttpRequest.BodyPublishers.ofString(json))
                // Use POST and attach the registration JSON.

                .build();
        // Create the immutable request object.

        CompletableFuture<HttpResponse<String>> first =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        // Start request one without waiting for its response.
        // The future will hold the response when it arrives.

        CompletableFuture<HttpResponse<String>> second =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        // Start request two using the same username.
        // Request one may still be running.

        CompletableFuture.allOf(first, second).join();
        // Wait until both requests complete.
        // A request failure causes join() to throw CompletionException.

        System.out.println("Test username: " + username);
        // Print the username so you can check its database record.

        System.out.println("First: " + first.join().statusCode());
        // Print the first response's HTTP status.

        System.out.println(first.join().body());
        // Print the first response body.

        System.out.println("Second: " + second.join().statusCode());
        // Print the second response's HTTP status.

        System.out.println(second.join().body());
        // Print the second response body.
    }
}