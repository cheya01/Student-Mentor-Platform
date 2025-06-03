package com.skill_mentor.root.mapper;

import com.skill_mentor.root.dto.MentorDTO;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.entity.UserEntity;

public class MentorEntityDTOMapper {

    public static MentorDTO map(MentorEntity mentorEntity) {
        MentorDTO dto = new MentorDTO();
        dto.setMentorId(mentorEntity.getMentorId());
        dto.setUserId(mentorEntity.getUser().getUserId());
        dto.setProfession(mentorEntity.getProfession());
        dto.setSubject(mentorEntity.getSubject());
        return dto;
    }

    public static MentorEntity map(MentorDTO dto, UserEntity user) {
        MentorEntity entity = new MentorEntity();
        entity.setMentorId(dto.getMentorId());
        entity.setUser(user);
        entity.setProfession(dto.getProfession());
        entity.setSubject(dto.getSubject());
        return entity;
    }
}
