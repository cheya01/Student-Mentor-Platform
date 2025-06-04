package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.SessionDTO;
import com.skill_mentor.root.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/session")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping()
    public ResponseEntity<SessionDTO> createSession(@RequestBody SessionDTO dto) {
        return ResponseEntity.ok(sessionService.createSession(dto));
    }

    @GetMapping()
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable Integer id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Integer id) {
        return sessionService.deleteSessionById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/end/{id}")
    public ResponseEntity<SessionDTO> endSession(@PathVariable Integer id) {
        return ResponseEntity.ok(sessionService.endSession(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionDTO> updateSessionById(@PathVariable Integer id, @RequestBody SessionDTO dto) {
        SessionDTO updated = sessionService.updateSessionById(id, dto);
        return ResponseEntity.ok(updated);
    }
}

