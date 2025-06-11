package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.MentorDTO;
import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.service.MentorService;
import com.skill_mentor.root.util.HelperMethods;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "api/v1/mentor")
public class MentorController {

    private final MentorService mentorService;
    private static final Logger logger = LoggerFactory.getLogger(MentorController.class);

    @Autowired
    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @PostMapping()
    public ResponseEntity<?> createMentor(@Valid @RequestBody MentorDTO mentorDTO) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        if (currentUser.getRole().getRole().equals("MENTOR")) {
            // Mentors can only create their own profile; override any provided userId
            mentorDTO.setUserId(currentUser.getUserId());

        } else if (currentUser.getRole().getRole().equals("ADMIN")) {
            // Admins must provide userId
            if (mentorDTO.getUserId() == null) {
                String message = "userId is required when creating a mentor profile as ADMIN.";
                logger.warn("Admin tried to create mentor without userId.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }

        MentorDTO savedMentor = mentorService.createMentor(mentorDTO);
        logger.info("User {} creating mentor {}", currentUser.getEmail(), savedMentor.getMentorId());
        return new ResponseEntity<>(savedMentor, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<MentorDTO>> getAllMentors() {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        List<MentorDTO> mentorDTOs = mentorService.getAllMentors();
        logger.info("User {} retrieving all students", currentUser.getEmail());
        return new ResponseEntity<>(mentorDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorDTO> getMentorById(@PathVariable Integer id) {
        MentorDTO mentor = mentorService.getMentorById(id);
        UserEntity currentUser = HelperMethods.getCurrentUser();

        if (currentUser.getRole().getRole().equals("MENTOR") &&
                !Objects.equals(mentor.getUserId(), currentUser.getUserId())) {
            logger.warn("User {} retrieving mentor {} failed", currentUser.getEmail(), id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        logger.info("User {} retrieving mentor {}", currentUser.getEmail(), id);
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentorDTO> updateMentorById(@PathVariable Integer id, @Valid @RequestBody MentorDTO mentorDTO) {
        MentorDTO existing = mentorService.getMentorById(id);
        UserEntity currentUser = HelperMethods.getCurrentUser();

        if (currentUser.getRole().getRole().equals("MENTOR") &&
                !Objects.equals(existing.getUserId(), currentUser.getUserId())) {
            logger.warn("User {} updating mentor {} failed", currentUser.getEmail(), id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        logger.info("User {} updating mentor {}", currentUser.getEmail(), id);
        return ResponseEntity.ok(mentorService.updateMentorById(id, mentorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentorById(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        logger.info("User {} deleting mentor {}", currentUser.getEmail(), id);
        boolean isDeleted = mentorService.deleteMentorById(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{mentorId}/earnings")
    public ResponseEntity<Map<String, Object>> getMentorEarnings(
            @PathVariable Integer mentorId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        UserEntity currentUser = HelperMethods.getCurrentUser();
        MentorDTO mentor = mentorService.getMentorById(mentorId);

        if (currentUser.getRole().getRole().equals("MENTOR") &&
                !Objects.equals(mentor.getUserId(), currentUser.getUserId())) {
            logger.warn("User {} reading earnings of mentor {} between {} and {} failed", currentUser.getEmail(), mentorId, startDate, endDate);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Double total = mentorService.getTotalEarnings(mentorId, startDate, endDate);
        Map<String, Object> response = Map.of(
                "mentorId", mentorId,
                "startDate", startDate,
                "endDate", endDate,
                "totalEarnings", total
        );
        logger.info("User {} reading earning of mentor {} between {} and {}", currentUser.getEmail(), mentor, startDate, endDate);
        return ResponseEntity.ok(response);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

