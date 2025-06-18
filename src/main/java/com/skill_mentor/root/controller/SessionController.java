package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.SessionDTO;
import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.entity.StudentEntity;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.repository.MentorRepository;
import com.skill_mentor.root.repository.StudentRepository;
import com.skill_mentor.root.service.SessionService;
import com.skill_mentor.root.util.HelperMethods;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/session")
public class SessionController {

    private final SessionService sessionService;
    private final MentorRepository mentorRepository;
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);
    private final StudentRepository studentRepository;

    @Autowired
    public SessionController(SessionService sessionService, MentorRepository mentorRepository, StudentRepository studentRepository) {
        this.sessionService = sessionService;
        this.mentorRepository = mentorRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping()
    public ResponseEntity<?> createSession(@RequestBody SessionDTO sessionDTO) {
        UserEntity currentUser = HelperMethods.getCurrentUser();

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));
            sessionDTO.setMentorId(mentor.getMentorId());

        } else if (currentUser.getRole().getRole().equals("ADMIN")) {
            // Admins must provide mentorId
            if (sessionDTO.getMentorId() == null) {
                String message = "mentorId is required when creating a new session as ADMIN.";
                logger.warn("Admin tried to create session without mentorId.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }

        SessionDTO savedSession = sessionService.createSession(sessionDTO);
        logger.info("User {} creating session {}", currentUser.getEmail(), savedSession.getSessionId());
        return new ResponseEntity<>(savedSession, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        UserEntity currentUser = HelperMethods.getCurrentUser();

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));
            logger.info("User {} retrieving all sessions with mentorId: {}", currentUser.getEmail(), mentor.getMentorId());
            return ResponseEntity.ok(sessionService.getSessionsByMentorId(mentor.getMentorId()));
        }

        logger.info("User {} retrieving all sessions", currentUser.getEmail());
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        SessionDTO session = sessionService.getSessionById(id);

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (!session.getMentorId().equals(mentor.getMentorId())) {
                logger.warn("Unauthorized session access by mentor {}", currentUser.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        logger.info("User {} retrieving session {}", currentUser.getEmail(), id);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        SessionDTO session = sessionService.getSessionById(id);

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (!session.getMentorId().equals(mentor.getMentorId())) {
                logger.warn("Unauthorized session delete attempt by mentor {}", currentUser.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        logger.info("User {} deleting session {}", currentUser.getEmail(), id);
        return sessionService.deleteSessionById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/end/{id}")
    public ResponseEntity<SessionDTO> endSession(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        SessionDTO session = sessionService.getSessionById(id);

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (!session.getMentorId().equals(mentor.getMentorId())) {
                logger.warn("Unauthorized session end attempt by mentor {}", currentUser.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        logger.info("User {} ending session {}", currentUser.getEmail(), id);
        return ResponseEntity.ok(sessionService.endSession(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionDTO> updateSessionById(@PathVariable Integer id, @RequestBody SessionDTO dto) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        SessionDTO session = sessionService.getSessionById(id);

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (!session.getMentorId().equals(mentor.getMentorId())) {
                logger.warn("Unauthorized session update attempt by mentor {}", currentUser.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        logger.info("User {} updating session {}", currentUser.getEmail(), id);
        return ResponseEntity.ok(sessionService.updateSessionById(id, dto));
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<SessionDTO>> getSessionsByMentorId(@PathVariable Integer mentorId) {
        UserEntity currentUser = HelperMethods.getCurrentUser();

        // If current user is a mentor, restrict access to only their own mentorId
        if (currentUser.getRole().getRole().equalsIgnoreCase("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (!mentor.getMentorId().equals(mentorId)) {
                logger.warn("Unauthorized session read attempt by mentor {}", currentUser.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }

        List<SessionDTO> sessions = sessionService.getSessionsByMentorId(mentorId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SessionDTO>> getSessionsByStudentId(@PathVariable Integer studentId) {
        UserEntity currentUser = HelperMethods.getCurrentUser();

        // If current user is a student, restrict access to only their own studentId
        if (currentUser.getRole().getRole().equalsIgnoreCase("STUDENT")) {
            StudentEntity student = studentRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            if (!student.getStudentId().equals(studentId)) {
                logger.warn("Unauthorized session read attempt by student {}", currentUser.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }

        List<SessionDTO> sessions = sessionService.getSessionsByStudentId(studentId);
        return ResponseEntity.ok(sessions);
    }
}

