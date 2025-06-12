package com.skill_mentor.root.repository;

import com.skill_mentor.root.entity.MentorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Integer> {
    Optional<MentorEntity> findByUser_UserId(Integer userId);
}
