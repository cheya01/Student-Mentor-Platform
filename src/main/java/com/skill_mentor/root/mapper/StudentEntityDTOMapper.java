package com.skill_mentor.root.mapper;

import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.StudentEntity;
import com.skill_mentor.root.entity.UserEntity;

public class StudentEntityDTOMapper {

    public static StudentDTO map(StudentEntity studentEntity) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentId(studentEntity.getStudentId());
        studentDTO.setUserId(studentEntity.getUser().getUserId()); // ✅
        studentDTO.setCollege(studentEntity.getCollege());
        studentDTO.setMajor(studentEntity.getMajor());
        return studentDTO;
    }

    public static StudentEntity map(StudentDTO studentDTO, UserEntity user) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStudentId(studentDTO.getStudentId());
        studentEntity.setUser(user);
        studentEntity.setCollege(studentDTO.getCollege());
        studentEntity.setMajor(studentDTO.getMajor());
        return studentEntity;
    }
}
