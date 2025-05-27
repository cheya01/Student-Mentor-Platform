package com.skill_mentor.root.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class StudentDTO {
    private Integer studentId;
    private Integer userId;
    private String college;
    private String major;

    public StudentDTO() {}

    public StudentDTO(Integer studentId, String college, String major) {
        this.studentId = studentId;
        this.college = college;
        this.major = major;
    }
}