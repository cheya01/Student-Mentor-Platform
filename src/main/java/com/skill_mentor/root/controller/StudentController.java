package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.service.StudentService;
import com.skill_mentor.root.service.UserService;
import com.skill_mentor.root.util.HelperMethods;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "api/v1/student")
public class StudentController {
    private final StudentService studentService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping()
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        UserEntity currentUser = HelperMethods.getCurrentUser();

        if (currentUser.getRole().getRole().equals("STUDENT")) {
            // Prevent students from creating a profile for others
            if (studentDTO.getUserId() != null && !Objects.equals(studentDTO.getUserId(), currentUser.getUserId())) {
                logger.warn("Student {} attempted to create profile for another user {}", currentUser.getUserId(), studentDTO.getUserId());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are only allowed to create your own student profile.");
            }

            // Force userId to their own ID
            studentDTO.setUserId(currentUser.getUserId());

        } else if (currentUser.getRole().getRole().equals("ADMIN")) {
            if (studentDTO.getUserId() == null) {
                logger.warn("Admin {} tried to create student without userId", currentUser.getUserId());
                return ResponseEntity.badRequest().body("userId is required when creating a student profile as ADMIN.");
            }
        }

        StudentDTO savedStudent = studentService.createStudent(studentDTO);
        if (savedStudent == null) {
            logger.error("Student creation failed: result is null.");
            return ResponseEntity.internalServerError().body("Failed to create student profile.");
        }

        logger.info("User {} created student profile for userId {}", currentUser.getEmail(), savedStudent.getUserId());
        return ResponseEntity.ok(savedStudent);
    }



    @GetMapping()
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        List<StudentDTO> studentDTOs = studentService.getAllStudents();
        logger.info("User {} retrieving all students", currentUser.getEmail());
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();

        StudentDTO student = studentService.getStudentById(id);
        if (student == null) {
            logger.warn("Student with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }

        if ("STUDENT".equalsIgnoreCase(currentUser.getRole().getRole())) {
            if (student.getUserId() == null || !Objects.equals(student.getUserId(), currentUser.getUserId())) {
                logger.warn("Unauthorized access: student {} attempted to access student {}", currentUser.getUserId(), id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }
        }

        logger.info("User {} retrieving student {}", currentUser.getEmail(), id);
        return ResponseEntity.ok(student);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentById(@PathVariable Integer id, @Valid @RequestBody StudentDTO studentDTO) {
        UserEntity currentUser = HelperMethods.getCurrentUser();

        StudentDTO existingStudent = studentService.getStudentById(id);
        if (existingStudent == null) {
            logger.warn("Student with ID {} not found for update by user {}", id, currentUser.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }

        if (currentUser.getRole().getRole().equals("STUDENT")) {
            // Only allow student to update their own profile
            if (!Objects.equals(existingStudent.getUserId(), currentUser.getUserId())) {
                logger.warn("Student {} attempted to update another student's profile (studentId: {})", currentUser.getUserId(), id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to update another student's profile.");
            }

            // Ensure userId in request body is set to current user’s ID
            studentDTO.setUserId(currentUser.getUserId());
        }

        StudentDTO updatedStudent = studentService.updateStudentById(id, studentDTO);
        if (updatedStudent == null) {
            logger.error("Student update failed for ID {}", id);
            return ResponseEntity.internalServerError().body("Failed to update student profile.");
        }

        logger.info("User {} updated student profile ID {}", currentUser.getEmail(), id);
        return ResponseEntity.ok(updatedStudent);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Integer id) {
        UserEntity currentUser = HelperMethods.getCurrentUser();
        logger.info("User {} deleting student {}", currentUser.getEmail(), id);
        boolean isDeleted = studentService.deleteStudentById(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
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

