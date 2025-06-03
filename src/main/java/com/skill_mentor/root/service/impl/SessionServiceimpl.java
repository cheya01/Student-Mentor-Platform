package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.SessionDTO;
import com.skill_mentor.root.entity.ClassRoomEntity;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.entity.SessionEntity;
import com.skill_mentor.root.entity.StudentEntity;
import com.skill_mentor.root.mapper.SessionEntityDTOMapper;
import com.skill_mentor.root.repository.ClassRoomRepository;
import com.skill_mentor.root.repository.MentorRepository;
import com.skill_mentor.root.repository.SessionRepository;
import com.skill_mentor.root.repository.StudentRepository;
import com.skill_mentor.root.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionServiceimpl implements SessionService {

    @Autowired private SessionRepository sessionRepository;
    @Autowired private ClassRoomRepository classRoomRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private MentorRepository mentorRepository;

    @Override
    public SessionDTO createSession(SessionDTO dto) {
        ClassRoomEntity classRoom = classRoomRepository.findById(dto.getClassRoomId())
                .orElseThrow(() -> new RuntimeException("Invalid classRoomId"));
        StudentEntity student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Invalid studentId"));
        MentorEntity mentor = mentorRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new RuntimeException("Invalid mentorId"));

        SessionEntity session = SessionEntityDTOMapper.map(dto, classRoom, student, mentor);
        session.setStartTime(LocalDateTime.now()); // optional: override with current time
        session.setStatus("SCHEDULED");
        return SessionEntityDTOMapper.map(sessionRepository.save(session));
    }

    @Override
    public List<SessionDTO> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(SessionEntityDTOMapper::map).toList();
    }

    @Override
    public SessionDTO getSessionById(Integer id) {
        return sessionRepository.findById(id)
                .map(SessionEntityDTOMapper::map)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    @Override
    public boolean deleteSessionById(Integer id) {
        if (sessionRepository.existsById(id)) {
            sessionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public SessionDTO endSession(Integer id) {
        SessionEntity session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        LocalDateTime endTime = LocalDateTime.now();
        session.setEndTime(endTime);
        session.setStatus("FINISHED");

        long seconds = Duration.between(session.getStartTime(), endTime).getSeconds();
        double hours = seconds / 3600.0;
        double rate = session.getClassRoom().getPerHourFee();
        double fee = hours * rate;

        session.setFee(Math.round(fee * 100.0) / 100.0); // round to 2 decimal places

        return SessionEntityDTOMapper.map(sessionRepository.save(session));
    }

    @Override
    public SessionDTO updateSessionById(Integer id, SessionDTO dto) {
        SessionEntity session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + id));

        if (dto.getTopic() != null) session.setTopic(dto.getTopic());
        if (dto.getLink() != null) session.setLink(dto.getLink());
        if (dto.getStatus() != null) session.setStatus(dto.getStatus());

        if (dto.getClassRoomId() != null) {
            ClassRoomEntity classRoom = classRoomRepository.findById(dto.getClassRoomId())
                    .orElseThrow(() -> new RuntimeException("Invalid classRoomId"));
            session.setClassRoom(classRoom);
        }

        if (dto.getStudentId() != null) {
            StudentEntity student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Invalid studentId"));
            session.setStudent(student);
        }

        if (dto.getMentorId() != null) {
            MentorEntity mentor = mentorRepository.findById(dto.getMentorId())
                    .orElseThrow(() -> new RuntimeException("Invalid mentorId"));
            session.setMentor(mentor);
        }

        if (dto.getStartTime() != null) session.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) session.setEndTime(dto.getEndTime());
        if (dto.getFee() != null) session.setFee(dto.getFee());

        return SessionEntityDTOMapper.map(sessionRepository.save(session));
    }

}

