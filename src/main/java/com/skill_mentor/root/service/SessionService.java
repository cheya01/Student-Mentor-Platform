package com.skill_mentor.root.service;

import com.skill_mentor.root.dto.SessionDTO;

import java.util.List;

public interface SessionService {

    SessionDTO createSession(SessionDTO sessionDTO);

    List<SessionDTO> getAllSessions();

    SessionDTO getSessionById(Integer id);

    boolean deleteSessionById(Integer id);

    SessionDTO endSession(Integer sessionId);

    SessionDTO updateSessionById(Integer id, SessionDTO dto);
}

