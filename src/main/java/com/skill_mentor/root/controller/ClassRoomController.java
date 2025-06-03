package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.ClassRoomDTO;
import com.skill_mentor.root.service.ClassRoomService;
import com.skill_mentor.root.validation.OnCreate;
import com.skill_mentor.root.validation.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/classroom")
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    @Autowired
    public ClassRoomController(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }

    @PostMapping()
    public ResponseEntity<ClassRoomDTO> createClassRoom(@Validated(OnCreate.class) @RequestBody ClassRoomDTO classRoomDTO) {
        ClassRoomDTO savedClassRoom = classRoomService.createClassRoom(classRoomDTO);
        return new ResponseEntity<>(savedClassRoom, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ClassRoomDTO>> getAllClassRooms() {
        List<ClassRoomDTO> classRoomDTOs = classRoomService.getAllClassRooms();
        return ResponseEntity.ok(classRoomDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassRoomDTO> getClassRoomById(@PathVariable Integer id) {
        ClassRoomDTO classRoom = classRoomService.getClassRoomById(id);
        return ResponseEntity.ok(classRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassRoomDTO> updateClassRoomById(@PathVariable Integer id, @Validated(OnUpdate.class) @RequestBody ClassRoomDTO classRoomDTO) {
        ClassRoomDTO updatedClassRoom = classRoomService.updateClassRoomById(id, classRoomDTO);
        return new ResponseEntity<>(updatedClassRoom, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassRoomById(@PathVariable Integer id) {
        boolean isDeleted = classRoomService.deleteClassRoomById(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
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
