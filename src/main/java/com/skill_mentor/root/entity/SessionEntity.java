package com.skill_mentor.root.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer sessionId;

    @ManyToOne
    @JoinColumn(name = "class_room_id", nullable = false)
    private ClassRoomEntity classRoom;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private MentorEntity mentor;

    @Column(length = 5000)
    private String link;

    private String topic;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = true)
    private LocalDateTime endTime;

    @Column(name = "fee", nullable = true)
    private Double fee;

    private String status;
}
