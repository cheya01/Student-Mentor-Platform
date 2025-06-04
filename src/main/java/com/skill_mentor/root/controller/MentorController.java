package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.MentorDTO;
import com.skill_mentor.root.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/mentor")
public class MentorController {

    private final MentorService mentorService;

    @Autowired
    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @PostMapping()
    public ResponseEntity<MentorDTO> createMentor(@Valid @RequestBody MentorDTO mentorDTO) {
        MentorDTO savedMentor = mentorService.createMentor(mentorDTO);
        return new ResponseEntity<>(savedMentor, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<MentorDTO>> getAllMentors() {
        List<MentorDTO> mentorDTOs = mentorService.getAllMentors();
        return new ResponseEntity<>(mentorDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorDTO> getMentorById(@PathVariable Integer id) {
        MentorDTO mentor = mentorService.getMentorById(id);
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentorDTO> updateMentorById(@PathVariable Integer id, @Valid @RequestBody MentorDTO mentorDTO) {
        MentorDTO updatedMentor = mentorService.updateMentorById(id, mentorDTO);
        if (updatedMentor != null) {
            return new ResponseEntity<>(updatedMentor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentorById(@PathVariable Integer id) {
        boolean isDeleted = mentorService.deleteMentorById(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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

