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
            // Students can only create their own profile; override any provided userId
            studentDTO.setUserId(currentUser.getUserId());

        } else if (currentUser.getRole().getRole().equals("ADMIN")) {
            // Admins must provide userId
            if (studentDTO.getUserId() == null) {
                String message = "userId is required when creating a student profile as ADMIN.";
                logger.warn("Admin tried to create student without userId.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        } else {
            logger.warn("Unauthorized role {} attempted to create student", currentUser.getRole().getRole());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your role is not permitted to create a student.");
        }

        StudentDTO savedStudent = studentService.createStudent(studentDTO);
        return new ResponseEntity<>(savedStudent, HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> studentDTOs = studentService.getAllStudents();
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Integer id) {
        StudentDTO student = studentService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudentById(@PathVariable Integer id, @Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudentById(id, studentDTO);
        if (updatedStudent != null) {
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Integer id) {
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

