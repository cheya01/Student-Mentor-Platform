package com.skill_mentor.root.repository;

import com.skill_mentor.root.entity.ClassRoomEntity;
import com.skill_mentor.root.entity.MentorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoomEntity, Integer> {
    boolean existsByTitle(String title);
    boolean existsByMentor(MentorEntity mentor);

}
