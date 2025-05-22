package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.StudentEntity;
import com.skill_mentor.root.mapper.StudentEntityDTOMapper;
import com.skill_mentor.root.repository.StudentRepository;
import com.skill_mentor.root.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceimpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        final StudentEntity studentEntity = StudentEntityDTOMapper.map(studentDTO);
        final StudentEntity savedEntity = studentRepository.save(studentEntity);
        return StudentEntityDTOMapper.map(savedEntity);
    }

    @Override
    public List<StudentDTO> getAllStudents(String city, Integer age) {
        List<StudentEntity> studentEntities = studentRepository.findAll();

        return studentEntities.stream()
                .filter(student -> {
                    boolean matchesCity = (city == null ||
                            student.getAddress() != null &&
                                    student.getAddress().toLowerCase().contains(city.toLowerCase()));

                    boolean matchesAge = (age == null || age.equals(student.getAge()));

                    return matchesCity && matchesAge;
                })
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
                    existing.setFirstName(studentDTO.getFirstName());
                    existing.setLastName(studentDTO.getLastName());
                    existing.setEmail(studentDTO.getEmail());
                    existing.setPhoneNumber(studentDTO.getPhoneNumber());
                    existing.setAddress(studentDTO.getAddress());
                    existing.setAge(studentDTO.getAge());
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
