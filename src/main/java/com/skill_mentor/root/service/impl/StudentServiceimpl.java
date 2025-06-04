package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.StudentEntity;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.mapper.StudentEntityDTOMapper;
import com.skill_mentor.root.repository.StudentRepository;
import com.skill_mentor.root.repository.UserRepository;
import com.skill_mentor.root.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceimpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceimpl.class);

    @Autowired
    public StudentServiceimpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        logger.info("Creating student with userId: {}", studentDTO.getUserId());
        try{
            UserEntity user = userRepository.findById(studentDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

            StudentEntity studentEntity = StudentEntityDTOMapper.map(studentDTO, user);
            StudentEntity savedEntity = studentRepository.save(studentEntity);
            return StudentEntityDTOMapper.map(savedEntity);
        } catch (Exception e) {
            logger.error("Error while creating student", e);
            return null;
        }
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        logger.info("Getting all students");
        try{
            return studentRepository.findAll().stream()
                    .map(StudentEntityDTOMapper::map)
                    .toList();
        } catch (Exception e) {
            logger.error("Error while getting all students", e);
            return null;
        }
    }

    @Override
    public StudentDTO getStudentById(Integer id) {
        logger.info("Getting student with id: {}", id);
        try {
            return studentRepository.findById(id)
                    .map(StudentEntityDTOMapper::map)
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error while getting student with id: {}", id, e);
            return null;
        }
    }

    @Override
    public StudentDTO updateStudentById(Integer id, StudentDTO studentDTO) {
        logger.info("Updating student with id: {}", id);
        try{
            return studentRepository.findById(id)
                    .map(existing -> {
                        existing.setCollege(studentDTO.getCollege());
                        existing.setMajor(studentDTO.getMajor());
                        return StudentEntityDTOMapper.map(studentRepository.save(existing));
                    }).orElse(null);
        } catch (Exception e) {
            logger.error("Error while updating student with id: {}", id, e);
            return null;
        }
    }

    @Override
    public boolean deleteStudentById(Integer id) {
        logger.info("Deleting student with id: {}", id);
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        logger.info("Student with id: {} not found", id);
        return false;
    }
}
