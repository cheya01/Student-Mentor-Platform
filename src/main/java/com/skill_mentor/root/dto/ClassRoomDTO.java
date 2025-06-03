package com.skill_mentor.root.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skill_mentor.root.validation.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassRoomDTO {

    @JsonProperty("class_room_id")
    private Integer classRoomId;

    @NotBlank(message = "Title must not be blank", groups = OnCreate.class)
    private String title;

    @NotNull(message = "Enrolled student count must not be null", groups = OnCreate.class)
    @JsonProperty("enrolled_student_count")
    private Integer enrolledStudentCount;

    @NotNull(message = "Mentor ID must not be null", groups = OnCreate.class)
    @JsonProperty("mentorId")
    private Integer mentorId;

    @NotNull(message = "Per hour fee is required", groups = OnCreate.class)
    private Double perHourFee;
}
