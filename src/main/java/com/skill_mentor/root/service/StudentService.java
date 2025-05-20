package com.skill_mentor.root.service;

import com.skill_mentor.root.dto.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);

    List<StudentDTO> getAllStudents(Integer age);

    StudentDTO getStudentById(Integer id);

    StudentDTO updateStudentById(Integer id, StudentDTO studentDTO);

    boolean deleteStudentById(Integer id);
}
