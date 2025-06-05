package com.skill_mentor.root.service;

import com.skill_mentor.root.dto.MentorDTO;

import java.time.LocalDate;
import java.util.List;

public interface MentorService {
    MentorDTO createMentor(MentorDTO mentorDTO);
    List<MentorDTO> getAllMentors();
    MentorDTO getMentorById(Integer id);
    MentorDTO updateMentorById(Integer id, MentorDTO mentorDTO);
    boolean deleteMentorById(Integer id);
    Double getTotalEarnings(Integer mentorId, LocalDate startDate, LocalDate endDate);
}
