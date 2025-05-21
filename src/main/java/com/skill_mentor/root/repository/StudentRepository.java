package com.skill_mentor.root.repository;

import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
}
