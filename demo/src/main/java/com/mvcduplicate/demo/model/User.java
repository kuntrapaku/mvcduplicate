package com.mvcduplicate.demo.model;
// Place this class in your existing model package.

import jakarta.persistence.*;
// Import the JPA annotations used to map this class to a database table.

@Entity
// Tell JPA that this class represents a persistent entity.

@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_users_username",
                columnNames = "username"
        )
)
// Map this entity to the users table.
// Declare that the username column must contain unique values.

public class User {
// Define your existing User entity.

    @Id
    // Identify the field that represents the primary key.

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Let the database generate the primary-key value during insertion.

    private Long id;
    // Store the database-generated user identifier.
    // Long allows a new, unsaved entity to have a null identifier.

    @Column(name = "username", nullable = false)
    // Map this field to the username column and declare it non-null.

    private String username;
    // Store the customer's login username.

    private String password;
    // Store the encoded password supplied by the service.
    // This field declaration does not encode the password itself.

    private String fullname;
    // Store the customer's full name.

    protected User() {
        // Provide the no-argument constructor required by JPA.
    }

    public User(String username, String password, String fullname) {
        // Allow application code to construct a user with initial values.

        this.username = username;
        // Assign the supplied username to this object's field.

        this.password = password;
        // Assign the supplied password value.

        this.fullname = fullname;
        // Assign the supplied full name.
    }

    public Long getId() {
        // Expose the user's identifier.

        return id;
        // Return the identifier stored in this object.
    }

    public void setId(Long id) {
        // Allow the identifier to be assigned.

        this.id = id;
        // Update this object's identifier.
    }

    public String getUsername() {
        // Expose the username.

        return username;
        // Return the stored username.
    }

    public void setUsername(String username) {
        // Allow the username to be assigned.

        this.username = username;
        // Update this object's username.
    }

    public String getPassword() {
        // Expose the stored password value to application code.

        return password;
        // Return the stored value.
    }

    public void setPassword(String password) {
        // Allow the service to assign an encoded password.

        this.password = password;
        // Store the supplied value without transforming it.
    }

    public String getFullname() {
        // Expose the customer's full name.

        return fullname;
        // Return the stored full name.
    }

    public void setFullname(String fullname) {
        // Allow the full name to be assigned.

        this.fullname = fullname;
        // Update this object's full name.
    }
}