package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dao.StudentDAO;
import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.repository.StudentRepository;
import com.skill_mentor.root.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceimpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentDAO studentDAO;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        return studentDAO.createStudent(studentDTO);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    @Override
    public StudentDTO getStudentById(Integer id) {
        return studentRepository.getStudentById(id);
    }

    @Override
    public StudentDTO updateStudentById(Integer id, StudentDTO studentDTO) {
        return studentRepository.updateStudentById(id, studentDTO);
    }

    @Override
    public boolean deleteStudentById(Integer id) {
        return studentRepository.deleteStudentById(id);
    }
}
