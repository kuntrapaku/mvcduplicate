package com.mvcduplicate.demo.config;
// WHERE: Store Spring configuration classes in this package.

import jakarta.servlet.DispatcherType;
// Identifies internal dispatches, including error handling.

import org.springframework.context.annotation.Bean;
// Registers a method's returned object as a Spring bean.

import org.springframework.context.annotation.Configuration;
// Marks this class as a source of Spring configuration.

import org.springframework.http.HttpMethod;
// Provides HTTP methods such as POST.

import org.springframework.security.config.Customizer;
// Provides default configuration options.

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// Provides methods for configuring HTTP security.

import org.springframework.security.web.SecurityFilterChain;
// Represents the security filters applied to incoming requests.

@Configuration
// Ask Spring to load the bean definitions in this class.

public class SecurityConfig {
// Define which requests need authentication.

    @Bean
    // Register the security filter chain with Spring.

    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        // Spring supplies the HTTP security builder.
        // Building the configuration can throw an exception.

        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/auth/register"));
        // For this Postman registration exercise, skip the CSRF-token
        // requirement on this endpoint only.
        // Other endpoints retain CSRF protection.

        http.authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        // Allow internal error dispatches to show the actual error.

                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        // Allow customers to register without already logging in.

                        .anyRequest().authenticated()
                // Require authentication for all other requests.
        );

        http.httpBasic(Customizer.withDefaults());
        // Enable standard HTTP Basic authentication for protected endpoints.
        // This does not yet connect authentication to your users table.

        return http.build();
        // Build and return the configured security filter chain.
    }
}