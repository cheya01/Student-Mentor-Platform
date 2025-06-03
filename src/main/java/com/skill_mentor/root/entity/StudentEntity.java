package com.skill_mentor.root.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students") // plural for convention
@Data
@NoArgsConstructor
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer studentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "college")
    private String college;

    @Column(name = "major")
    private String major;

    public StudentEntity(Integer studentId, UserEntity user, String college, String major) {
        this.studentId = studentId;
        this.user = user;
        this.college = college;
        this.major = major;
    }
}

