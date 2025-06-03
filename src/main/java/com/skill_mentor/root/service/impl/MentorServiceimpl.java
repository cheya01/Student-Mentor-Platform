package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.MentorDTO;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.mapper.MentorEntityDTOMapper;
import com.skill_mentor.root.repository.MentorRepository;
import com.skill_mentor.root.repository.UserRepository;
import com.skill_mentor.root.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorServiceimpl implements MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public MentorDTO createMentor(MentorDTO mentorDTO) {
        UserEntity user = userRepository.findById(mentorDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        MentorEntity mentorEntity = MentorEntityDTOMapper.map(mentorDTO, user);
        MentorEntity savedEntity = mentorRepository.save(mentorEntity);
        return MentorEntityDTOMapper.map(savedEntity);
    }

    @Override
    public List<MentorDTO> getAllMentors() {
        return mentorRepository.findAll().stream()
                .map(MentorEntityDTOMapper::map)
                .toList();
    }

    @Override
    public MentorDTO getMentorById(Integer id) {
        return mentorRepository.findById(id)
                .map(MentorEntityDTOMapper::map)
                .orElse(null);
    }

    @Override
    public MentorDTO updateMentorById(Integer id, MentorDTO mentorDTO) {
        return mentorRepository.findById(id)
                .map(existing -> {
                    existing.setProfession(mentorDTO.getProfession());
                    existing.setSubject(mentorDTO.getSubject());
                    return MentorEntityDTOMapper.map(mentorRepository.save(existing));
                }).orElse(null);
    }

    @Override
    public boolean deleteMentorById(Integer id) {
        if (mentorRepository.existsById(id)) {
            mentorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
