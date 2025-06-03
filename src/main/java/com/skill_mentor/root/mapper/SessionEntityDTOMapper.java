package com.skill_mentor.root.mapper;

import com.skill_mentor.root.dto.SessionDTO;
import com.skill_mentor.root.entity.ClassRoomEntity;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.entity.SessionEntity;
import com.skill_mentor.root.entity.StudentEntity;

public class SessionEntityDTOMapper {

    public static SessionDTO map(SessionEntity entity) {
        SessionDTO dto = new SessionDTO();
        dto.setSessionId(entity.getSessionId());
        dto.setClassRoomId(entity.getClassRoom().getClassRoomId());
        dto.setStudentId(entity.getStudent().getStudentId());
        dto.setMentorId(entity.getMentor().getMentorId());
        dto.setTopic(entity.getTopic());
        dto.setLink(entity.getLink());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setFee(entity.getFee());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public static SessionEntity map(SessionDTO dto, ClassRoomEntity classRoom,
                                    StudentEntity student, MentorEntity mentor) {
        SessionEntity entity = new SessionEntity();
        entity.setSessionId(dto.getSessionId());
        entity.setClassRoom(classRoom);
        entity.setStudent(student);
        entity.setMentor(mentor);
        entity.setTopic(dto.getTopic());
        entity.setLink(dto.getLink());
        entity.setStartTime(dto.getStartTime());
        entity.setStatus("SCHEDULED");
        return entity;
    }
}
