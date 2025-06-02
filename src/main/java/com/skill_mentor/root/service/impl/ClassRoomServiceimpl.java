package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.ClassRoomDTO;
import com.skill_mentor.root.entity.ClassRoomEntity;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.exception.ClassRoomException;
import com.skill_mentor.root.mapper.ClassRoomEntityDTOMapper;
import com.skill_mentor.root.repository.ClassRoomRepository;
import com.skill_mentor.root.repository.MentorRepository;
import com.skill_mentor.root.service.ClassRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassRoomServiceimpl implements ClassRoomService {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Override
    public List<ClassRoomDTO> getAllClassRooms() {
        final List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAll();
        return classRoomEntities.stream()
                .map(ClassRoomEntityDTOMapper::map)
                .toList();
    }

    @Override
    public ClassRoomDTO getClassRoomById(Integer id) {
        final Optional<ClassRoomEntity> classRoomEntity = classRoomRepository.findById(id);
        if (classRoomEntity.isEmpty()) {
            throw new ClassRoomException("ClassRoom not found");
        }
        return ClassRoomEntityDTOMapper.map(classRoomEntity.get());
    }

    @Override
    public boolean deleteClassRoomById(Integer id) {
        if (classRoomRepository.existsById(id)) {
            classRoomRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ClassRoomDTO updateClassRoomById(Integer id, ClassRoomDTO classRoomDTO) {
        return classRoomRepository.findById(id)
                .map(existing -> {
                    if (classRoomDTO.getTitle() != null)
                        existing.setTitle(classRoomDTO.getTitle());

                    if (classRoomDTO.getEnrolledStudentCount() != null)
                        existing.setEnrolledStudentCount(classRoomDTO.getEnrolledStudentCount());

                    if (classRoomDTO.getPerHourFee() != null)
                        existing.setPerHourFee(classRoomDTO.getPerHourFee());

                    if (classRoomDTO.getMentorId() != null) {
                        MentorEntity mentor = mentorRepository.findById(classRoomDTO.getMentorId())
                                .orElseThrow(() -> new ClassRoomException("Mentor not found with ID: " + classRoomDTO.getMentorId()));
                        existing.setMentor(mentor);
                    }

                    return ClassRoomEntityDTOMapper.map(classRoomRepository.save(existing));
                })
                .orElseThrow(() -> new ClassRoomException("ClassRoom not found with ID: " + id));
    }


    @Override
    public ClassRoomDTO createClassRoom(ClassRoomDTO classRoomDTO) {
        MentorEntity mentor = mentorRepository.findById(classRoomDTO.getMentorId())
                .orElseThrow(() -> new ClassRoomException("Mentor not found with ID: " + classRoomDTO.getMentorId()));

        if (classRoomRepository.existsByTitle(classRoomDTO.getTitle())) {
            throw new ClassRoomException("A class with this title already exists.");
        }
        if (classRoomRepository.existsByMentor(mentor)) {
            throw new ClassRoomException("Mentor is already managing a classroom.");
        }

        ClassRoomEntity classRoomEntity = ClassRoomEntityDTOMapper.map(classRoomDTO, mentor);
        ClassRoomEntity savedEntity = classRoomRepository.save(classRoomEntity);
        return ClassRoomEntityDTOMapper.map(savedEntity);
    }
}
