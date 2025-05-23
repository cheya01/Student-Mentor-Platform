package com.skill_mentor.root.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

public @Data class StudentDTO {
    private Integer studentId;
    private String firstName;
    private String lastName;
    @Valid
    @NotNull(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    private String email;
    private String phoneNumber;
    private String address;
    private Integer age;

    public StudentDTO(Integer studentId, String firstName, String lastName, String email, String phoneNumber, String address, Integer age) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.age = age;
    }

    public StudentDTO() {

    }
}