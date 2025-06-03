package com.skill_mentor.root.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mentors")
@Data
@NoArgsConstructor
public class MentorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_id")
    private Integer mentorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    private String profession;
    private String subject;

    public MentorEntity(Integer mentorId, UserEntity user, String profession, String subject) {
        this.mentorId = mentorId;
        this.user = user;
        this.profession = profession;
        this.subject = subject;
    }
}
