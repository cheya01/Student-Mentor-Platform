package com.skill_mentor.root.dao;

import com.skill_mentor.root.dto.StudentDTO;

import java.util.List;

public interface StudentDAO {
    StudentDTO createStudent(StudentDTO studentDTO);

    List<StudentDTO> getAllStudents();

    StudentDTO getStudentById(Integer id);

    StudentDTO updateStudentById(Integer id, StudentDTO studentDTO);

    boolean deleteStudentById(Integer id);
}
