package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.StudentEntity;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.mapper.StudentEntityDTOMapper;
import com.skill_mentor.root.repository.StudentRepository;
import com.skill_mentor.root.repository.UserRepository;
import com.skill_mentor.root.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceimpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        UserEntity user = userRepository.findById(studentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        StudentEntity studentEntity = StudentEntityDTOMapper.map(studentDTO, user);
        StudentEntity savedEntity = studentRepository.save(studentEntity);
        return StudentEntityDTOMapper.map(savedEntity);
    }

    @Override
    public List<StudentDTO> getAllStudents(String city, Integer age) {
        // city and age are no longer part of student schema — so filter is removed
        return studentRepository.findAll().stream()
                .map(StudentEntityDTOMapper::map)
                .toList();
    }

    @Override
    public StudentDTO getStudentById(Integer id) {
        return studentRepository.findById(id)
                .map(StudentEntityDTOMapper::map)
                .orElse(null);
    }

    @Override
    public StudentDTO updateStudentById(Integer id, StudentDTO studentDTO) {
        return studentRepository.findById(id)
                .map(existing -> {
                    existing.setCollege(studentDTO.getCollege());
                    existing.setMajor(studentDTO.getMajor());
                    return StudentEntityDTOMapper.map(studentRepository.save(existing));
                }).orElse(null);
    }

    @Override
    public boolean deleteStudentById(Integer id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
