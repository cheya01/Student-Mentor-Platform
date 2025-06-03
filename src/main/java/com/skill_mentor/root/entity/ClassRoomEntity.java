package com.skill_mentor.root.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "classroom")
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_room_id")
    private Integer classRoomId;

    @NotNull(message = "Title must not be null")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "Enrolled student count must not be null")
    @Min(value = 0, message = "Enrolled student count must be zero or more")
    @Column(name = "enrolled_student_count", nullable = false)
    private Integer enrolledStudentCount;

    @NotNull(message = "Per hour fee must not be null")
    @Min(value = 0, message = "Per hour fee must be zero or more")
    @Column(name = "per_hour_fee", nullable = false)
    private Double perHourFee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", nullable = false)
    private MentorEntity mentor;

    @OneToMany(mappedBy = "classRoomEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SessionEntity> sessionEntityList = new ArrayList<>();
}
