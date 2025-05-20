package com.skill_mentor.root.repository;

import com.skill_mentor.root.dto.StudentDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class StudentRepository {
    private List<StudentDTO> students = new ArrayList<>();

    public StudentDTO createStudent(StudentDTO studentDTO) {
        students.add(studentDTO);
        return studentDTO;
    }

    public List<StudentDTO> getAllStudents(Integer age) {
        return students.stream().filter(stu->age == null || stu.getAge().equals(age)).toList();
    }

    public StudentDTO getStudentById(Integer id) {
        Optional<StudentDTO> studentDTOOptional = students.stream().filter(stu-> Objects.equals(stu.getStudentId(), id)).findFirst();
        return studentDTOOptional.orElse(null);
    }

    public StudentDTO updateStudentById(Integer id, StudentDTO studentDTO) {
        Optional<StudentDTO> optionalStudent = students.stream()
                .filter(stu -> Objects.equals(stu.getStudentId(), id))
                .findFirst();

        if (optionalStudent.isPresent()) {
            StudentDTO existingStudent = optionalStudent.get();
            existingStudent.setFirstName(studentDTO.getFirstName());
            existingStudent.setLastName(studentDTO.getLastName());
            existingStudent.setEmail(studentDTO.getEmail());
            existingStudent.setPhoneNumber(studentDTO.getPhoneNumber());
            existingStudent.setAddress(studentDTO.getAddress());
            existingStudent.setAge(studentDTO.getAge());
            return existingStudent;
        } else {
            return null; // or throw a custom exception
        }
    }

    public boolean deleteStudentById(Integer id) {
        return students.removeIf(student -> Objects.equals(student.getStudentId(), id));
    }
}
