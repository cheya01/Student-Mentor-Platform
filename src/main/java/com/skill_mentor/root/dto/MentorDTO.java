package com.skill_mentor.root.dto;

import lombok.Data;

@Data
public class MentorDTO {
    private Integer mentorId;
    private Integer userId;
    private String profession;
    private String subject;

    public MentorDTO() {}

    public MentorDTO(Integer mentorId, Integer userId, String profession, String subject) {
        this.mentorId = mentorId;
        this.userId = userId;
        this.profession = profession;
        this.subject = subject;
    }
}
