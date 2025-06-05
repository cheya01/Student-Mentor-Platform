package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.MentorDTO;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.mapper.MentorEntityDTOMapper;
import com.skill_mentor.root.repository.MentorRepository;
import com.skill_mentor.root.repository.SessionRepository;
import com.skill_mentor.root.repository.UserRepository;
import com.skill_mentor.root.service.MentorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MentorServiceimpl implements MentorService {

    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private static final Logger logger = LoggerFactory.getLogger(MentorServiceimpl.class);

    @Autowired
    public MentorServiceimpl(MentorRepository mentorRepository, UserRepository userRepository, SessionRepository sessionRepository) {
        this.mentorRepository = mentorRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public MentorDTO createMentor(MentorDTO mentorDTO) {
        logger.info("Creating mentor with userId: {}", mentorDTO.getUserId());
        try {
            UserEntity user = userRepository.findById(mentorDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

            MentorEntity mentorEntity = MentorEntityDTOMapper.map(mentorDTO, user);
            MentorEntity savedEntity = mentorRepository.save(mentorEntity);
            return MentorEntityDTOMapper.map(savedEntity);
        } catch (Exception e) {
            logger.error("Error while creating mentor", e);
            return null;
        }
    }

    @Override
    public List<MentorDTO> getAllMentors() {
        logger.info("Getting all mentors");
        try{
            return mentorRepository.findAll().stream()
                    .map(MentorEntityDTOMapper::map)
                    .toList();
        } catch (Exception e) {
            logger.error("Error while getting mentors", e);
            return null;
        }
    }

    @Override
    public MentorDTO getMentorById(Integer id) {
        logger.info("Getting mentor with id: {}", id);
        try{
            return mentorRepository.findById(id)
                    .map(MentorEntityDTOMapper::map)
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error while getting mentor", e);
            return null;
        }
    }

    @Override
    public MentorDTO updateMentorById(Integer id, MentorDTO mentorDTO) {
        logger.info("Updating mentor with id: {}", id);
        try{
            return mentorRepository.findById(id)
                    .map(existing -> {
                        existing.setProfession(mentorDTO.getProfession());
                        existing.setSubject(mentorDTO.getSubject());
                        return MentorEntityDTOMapper.map(mentorRepository.save(existing));
                    }).orElse(null);
        } catch (Exception e) {
            logger.error("Error while updating mentor", e);
            return null;
        }
    }

    @Override
    public boolean deleteMentorById(Integer id) {
        logger.info("Deleting mentor with id: {}", id);
        if (mentorRepository.existsById(id)) {
            mentorRepository.deleteById(id);
            return true;
        }
        logger.info("Mentor with id: {} not found", id);
        return false;
    }

    @Override
    public Double getTotalEarnings(Integer mentorId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay().minusSeconds(1);
        return sessionRepository.getTotalEarningsByMentorIdAndDateRange(mentorId, start, end);
    }
}
