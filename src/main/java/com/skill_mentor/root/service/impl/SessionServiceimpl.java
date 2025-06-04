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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionServiceimpl implements SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionServiceimpl.class);

     private final SessionRepository sessionRepository;
     private final ClassRoomRepository classRoomRepository;
     private final StudentRepository studentRepository;
     private final MentorRepository mentorRepository;

     @Autowired
     public SessionServiceimpl(SessionRepository sessionRepository,
                               ClassRoomRepository classRoomRepository,
                               StudentRepository studentRepository,
                               MentorRepository mentorRepository) {
         this.sessionRepository = sessionRepository;
         this.classRoomRepository = classRoomRepository;
         this.studentRepository = studentRepository;
         this.mentorRepository = mentorRepository;
     }

    @Override
    public SessionDTO createSession(SessionDTO dto) {
        try {
            logger.info("Creating session for classRoomId={}, studentId={}, mentorId={}",
                    dto.getClassRoomId(), dto.getStudentId(), dto.getMentorId());

            ClassRoomEntity classRoom = classRoomRepository.findById(dto.getClassRoomId())
                    .orElseThrow(() -> new RuntimeException("Invalid classRoomId: " + dto.getClassRoomId()));
            StudentEntity student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Invalid studentId: " + dto.getStudentId()));
            MentorEntity mentor = mentorRepository.findById(dto.getMentorId())
                    .orElseThrow(() -> new RuntimeException("Invalid mentorId: " + dto.getMentorId()));

            SessionEntity session = SessionEntityDTOMapper.map(dto, classRoom, student, mentor);
            session.setStartTime(LocalDateTime.now());
            session.setStatus("SCHEDULED");

            SessionEntity saved = sessionRepository.save(session);
            logger.info("Session created successfully with ID {}", saved.getSessionId());
            return SessionEntityDTOMapper.map(saved);

        } catch (Exception e) {
            logger.error("Failed to create session", e);
            throw e;
        }
    }

    @Override
    public List<SessionDTO> getAllSessions() {
        logger.info("Fetching all sessions");
        return sessionRepository.findAll().stream()
                .map(SessionEntityDTOMapper::map)
                .toList();
    }

    @Override
    public SessionDTO getSessionById(Integer id) {
        try {
            logger.info("Fetching session with ID {}", id);
            return sessionRepository.findById(id)
                    .map(SessionEntityDTOMapper::map)
                    .orElseThrow(() -> new RuntimeException("Session not found with ID: " + id));
        } catch (Exception e) {
            logger.error("Failed to fetch session with ID {}", id, e);
            throw e;
        }
    }

    @Override
    public boolean deleteSessionById(Integer id) {
        try {
            logger.info("Deleting session with ID {}", id);
            if (sessionRepository.existsById(id)) {
                sessionRepository.deleteById(id);
                logger.info("Session deleted with ID {}", id);
                return true;
            }
            logger.warn("Session not found for deletion with ID {}", id);
            return false;
        } catch (Exception e) {
            logger.error("Error deleting session with ID {}", id, e);
            throw e;
        }
    }

    @Override
    public SessionDTO endSession(Integer id) {
        try {
            logger.info("Ending session with ID {}", id);
            SessionEntity session = sessionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Session not found with ID: " + id));

            LocalDateTime endTime = LocalDateTime.now();
            session.setEndTime(endTime);
            session.setStatus("FINISHED");

            long seconds = Duration.between(session.getStartTime(), endTime).getSeconds();
            double hours = seconds / 3600.0;
            double rate = session.getClassRoom().getPerHourFee();
            double fee = Math.round(hours * rate * 100.0) / 100.0;

            session.setFee(fee);

            SessionEntity saved = sessionRepository.save(session);
            logger.info("Session ended. Fee calculated: {}, Duration: {} seconds", fee, seconds);
            return SessionEntityDTOMapper.map(saved);

        } catch (Exception e) {
            logger.error("Failed to end session with ID {}", id, e);
            throw e;
        }
    }

    @Override
    public SessionDTO updateSessionById(Integer id, SessionDTO dto) {
        try {
            logger.info("Updating session with ID {}", id);
            SessionEntity session = sessionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Session not found with ID: " + id));

            if (dto.getTopic() != null) session.setTopic(dto.getTopic());
            if (dto.getLink() != null) session.setLink(dto.getLink());
            if (dto.getStatus() != null) session.setStatus(dto.getStatus());

            if (dto.getClassRoomId() != null) {
                ClassRoomEntity classRoom = classRoomRepository.findById(dto.getClassRoomId())
                        .orElseThrow(() -> new RuntimeException("Invalid classRoomId: " + dto.getClassRoomId()));
                session.setClassRoom(classRoom);
            }

            if (dto.getStudentId() != null) {
                StudentEntity student = studentRepository.findById(dto.getStudentId())
                        .orElseThrow(() -> new RuntimeException("Invalid studentId: " + dto.getStudentId()));
                session.setStudent(student);
            }

            if (dto.getMentorId() != null) {
                MentorEntity mentor = mentorRepository.findById(dto.getMentorId())
                        .orElseThrow(() -> new RuntimeException("Invalid mentorId: " + dto.getMentorId()));
                session.setMentor(mentor);
            }

            if (dto.getStartTime() != null) session.setStartTime(dto.getStartTime());
            if (dto.getEndTime() != null) session.setEndTime(dto.getEndTime());
            if (dto.getFee() != null) session.setFee(dto.getFee());

            SessionEntity updated = sessionRepository.save(session);
            logger.info("Session updated with ID {}", updated.getSessionId());
            return SessionEntityDTOMapper.map(updated);

        } catch (Exception e) {
            logger.error("Failed to update session with ID {}", id, e);
            throw e;
        }
    }
}
