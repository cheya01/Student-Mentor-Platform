package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.SessionDTO;
import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.repository.MentorRepository;
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

    @Autowired
    public SessionController(SessionService sessionService, MentorRepository mentorRepository) {
        this.sessionService = sessionService;
        this.mentorRepository = mentorRepository;
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
        } else {
            logger.warn("Unauthorized role {} attempted to create session", currentUser.getRole().getRole());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your role is not permitted to create a session.");
        }

        SessionDTO savedSession = sessionService.createSession(sessionDTO);
        logger.info("User {} creating session {}", currentUser.getEmail(), savedSession.getSessionId());
        return new ResponseEntity<>(savedSession, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        UserEntity currentUser = HelperMethods.getCurrentUser();

        if (currentUser.getRole().getRole().equals("ADMIN")) {
            logger.info("User {} retrieving all sessions", currentUser.getEmail());
            return ResponseEntity.ok(sessionService.getAllSessions());
        }

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));
            logger.info("User {} retrieving all sessions with mentorId: {}", currentUser.getEmail(), mentor.getMentorId());
            return ResponseEntity.ok(sessionService.getSessionsByMentorId(mentor.getMentorId()));
        }

        logger.warn("something went wrong while retrieving all sessions");
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        SessionDTO session = sessionService.getSessionById(id);

        if (currentUser.getRole().getRole().equals("ADMIN")) {
            logger.info("User {} retrieving session {}", currentUser.getEmail(), id);
            return ResponseEntity.ok(session);
        }

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (session.getMentorId().equals(mentor.getMentorId())) {
                logger.info("User {} retrieving session {}", currentUser.getEmail(), id);
                return ResponseEntity.ok(session);
            }
        }

        logger.warn("something went wrong while retrieving session by ID");
        return ResponseEntity.status(403).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        SessionDTO session = sessionService.getSessionById(id);

        if (currentUser.getRole().getRole().equals("ADMIN")) {
            logger.info("User {} deleting session {}", currentUser.getEmail(), id);
            return sessionService.deleteSessionById(id)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        }

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (session.getMentorId().equals(mentor.getMentorId())) {
                logger.info("User {} deleting session {}", currentUser.getEmail(), id);
                return sessionService.deleteSessionById(id)
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.notFound().build();
            }
        }

        logger.warn("something went wrong while deleting session {}", id);
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/end/{id}")
    public ResponseEntity<SessionDTO> endSession(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        SessionDTO session = sessionService.getSessionById(id);

        if (currentUser.getRole().getRole().equals("ADMIN")) {
            logger.info("User {} ending session {}", currentUser.getEmail(), id);
            return ResponseEntity.ok(sessionService.endSession(id));
        }

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (session.getMentorId().equals(mentor.getMentorId())) {
                logger.info("User {} ending session {}", currentUser.getEmail(), id);
                return ResponseEntity.ok(sessionService.endSession(id));
            }
        }

        logger.warn("something went wrong while ending session {}", id);
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionDTO> updateSessionById(@PathVariable Integer id, @RequestBody SessionDTO dto) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        SessionDTO session = sessionService.getSessionById(id);

        if (currentUser.getRole().getRole().equals("ADMIN")) {
            logger.info("User {} updating session {}", currentUser.getEmail(), id);
            return ResponseEntity.ok(sessionService.updateSessionById(id, dto));
        }

        if (currentUser.getRole().getRole().equals("MENTOR")) {
            MentorEntity mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("Mentor profile not found"));

            if (session.getMentorId().equals(mentor.getMentorId())) {
                logger.info("User {} updating session {}", currentUser.getEmail(), id);
                return ResponseEntity.ok(sessionService.updateSessionById(id, dto));
            }
        }

        logger.warn("something went wrong while updating session {}", id);
        return ResponseEntity.status(403).build();
    }
}

