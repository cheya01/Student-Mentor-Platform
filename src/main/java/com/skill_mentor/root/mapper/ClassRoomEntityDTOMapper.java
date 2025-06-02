package com.skill_mentor.root.mapper;

import com.skill_mentor.root.dto.ClassRoomDTO;
import com.skill_mentor.root.entity.ClassRoomEntity;
import com.skill_mentor.root.entity.MentorEntity;

public class ClassRoomEntityDTOMapper {

    // Entity → DTO
    public static ClassRoomDTO map(ClassRoomEntity entity) {
        ClassRoomDTO dto = new ClassRoomDTO();
        dto.setClassRoomId(entity.getClassRoomId());
        dto.setTitle(entity.getTitle());
        dto.setEnrolledStudentCount(entity.getEnrolledStudentCount());
        dto.setPerHourFee(entity.getPerHourFee());
        if (entity.getMentor() != null) {
            dto.setMentorId(entity.getMentor().getMentorId());
        }
        return dto;
    }

    // DTO → Entity (with MentorEntity injected)
    public static ClassRoomEntity map(ClassRoomDTO dto, MentorEntity mentor) {
        ClassRoomEntity entity = new ClassRoomEntity();
        entity.setClassRoomId(dto.getClassRoomId());
        entity.setTitle(dto.getTitle());
        entity.setEnrolledStudentCount(dto.getEnrolledStudentCount());
        entity.setPerHourFee(dto.getPerHourFee());
        entity.setMentor(mentor);
        return entity;
    }

    // Optional: DTO → Entity (without mentor)
    public static ClassRoomEntity map(ClassRoomDTO dto) {
        ClassRoomEntity entity = new ClassRoomEntity();
        entity.setClassRoomId(dto.getClassRoomId());
        entity.setTitle(dto.getTitle());
        entity.setEnrolledStudentCount(dto.getEnrolledStudentCount());
        entity.setPerHourFee(dto.getPerHourFee());
        return entity;
    }
}
