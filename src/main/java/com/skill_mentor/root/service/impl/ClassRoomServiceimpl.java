package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.ClassRoomDTO;
import com.skill_mentor.root.entity.ClassRoomEntity;
import com.skill_mentor.root.entity.MentorEntity;
import com.skill_mentor.root.exception.ClassRoomException;
import com.skill_mentor.root.mapper.ClassRoomEntityDTOMapper;
import com.skill_mentor.root.repository.ClassRoomRepository;
import com.skill_mentor.root.repository.MentorRepository;
import com.skill_mentor.root.service.ClassRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassRoomServiceimpl implements ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final MentorRepository mentorRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClassRoomServiceimpl.class);

    @Autowired
    public ClassRoomServiceimpl(ClassRoomRepository classRoomRepository, MentorRepository mentorRepository) {
        this.classRoomRepository = classRoomRepository;
        this.mentorRepository = mentorRepository;
    }

    @Override
    public ClassRoomDTO createClassRoom(ClassRoomDTO classRoomDTO) {
        logger.info("Creating ClassRoom: {}", classRoomDTO.getTitle());
        try{
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
        } catch (ClassRoomException e) {
            logger.error("Error while creating classroom", e);
            return null;
        }
    }

    @Override
    public List<ClassRoomDTO> getAllClassRooms() {
        logger.info("Getting all Class Rooms");
        try{
            final List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAll();
            return classRoomEntities.stream()
                    .map(ClassRoomEntityDTOMapper::map)
                    .toList();
        } catch (Exception e){
            logger.error("Error while getting all Class Rooms", e);
            return null;
        }
    }

    @Override
    public ClassRoomDTO getClassRoomById(Integer id) {
        logger.info("Getting Class Room by ID: {}", id);
        try{
            final Optional<ClassRoomEntity> classRoomEntity = classRoomRepository.findById(id);
            if (classRoomEntity.isEmpty()) {
                throw new ClassRoomException("ClassRoom not found");
            }
            return ClassRoomEntityDTOMapper.map(classRoomEntity.get());
        } catch (Exception e){
            logger.error("Error while getting Class Room", e);
            return null;
        }
    }

    @Override
    public ClassRoomDTO updateClassRoomById(Integer id, ClassRoomDTO classRoomDTO) {
        logger.info("Updating Class Room: {}", id);
        try{
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
        } catch (Exception e){
            logger.error("Error while updating Class Room", e);
            return null;
        }
    }

    @Override
    public boolean deleteClassRoomById(Integer id) {
        logger.info("Deleting Class Room: {}", id);
        if (classRoomRepository.existsById(id)) {
            classRoomRepository.deleteById(id);
            return true;
        }
        logger.info("Class room with id: {} not found", id);
        return false;
    }
}
