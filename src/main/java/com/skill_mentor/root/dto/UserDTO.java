package com.skill_mentor.root.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    // Basic Identification
    private Integer userId;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    // Authentication and Contact
    @Valid
    @NotNull(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @JsonIgnore
    private String passwordHash;

    private String role; // STUDENT, MENTOR, ADMIN
    private Boolean isActive;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    // Profile Details
    private String phoneNumber;
    private String address;
    private String NIC;

    // Default constructor
    public UserDTO() {}

    // All-args constructor
    public UserDTO(Integer userId, String firstName, String lastName, String email, String passwordHash,
                   String role, Boolean isActive, LocalDateTime createdAt, LocalDateTime lastLogin,
                   String phoneNumber, String address, String NIC) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.NIC = NIC;
    }
}
