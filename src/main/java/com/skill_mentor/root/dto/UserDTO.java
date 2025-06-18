package com.skill_mentor.root.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skill_mentor.root.validation.OnCreate;
import com.skill_mentor.root.validation.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
public class  UserDTO {

    // Basic Identification
    private Integer userId;

    @NotBlank(message = "First name is mandatory", groups = OnCreate.class)
    private String firstName;

    @NotBlank(message = "Last name is mandatory", groups = OnCreate.class)
    private String lastName;

    // Authentication and Contact
    @Valid
    @NotNull(message = "Email is mandatory", groups = OnCreate.class)
    @NotBlank(message = "Email is mandatory", groups = OnCreate.class)
    @Email(message = "Email should be valid")
    private String email;

    @Valid
    @ValidPassword
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Integer roleId;  // ID of RoleEntity (e.g. 1=STUDENT, 2=MENTOR, etc.)

    private Boolean isActive;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    // Profile Details
    private String phoneNumber;
    private String address;
    private String NIC;

    private LocalDate dateOfBirth;
    private String gender;

    public Integer getAge() {
        return (dateOfBirth != null)
                ? Period.between(dateOfBirth, LocalDate.now()).getYears()
                : null;
    }

    // Default constructor
    public UserDTO() {}

    // All-args constructor
    public UserDTO(Integer userId, String firstName, String lastName, String email, String password,
                   Integer roleId, Boolean isActive, LocalDateTime createdAt, LocalDateTime lastLogin,
                   String phoneNumber, String address, String NIC) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.NIC = NIC;
    }
}
