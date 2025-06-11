package com.skill_mentor.root.repository;

import com.skill_mentor.root.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Integer> {
    @Query("SELECT COALESCE(SUM(s.fee), 0) FROM SessionEntity s " +
            "WHERE s.mentor.id = :mentorId " +
            "AND s.endTime BETWEEN :start AND :end " +
            "AND s.status = 'FINISHED'")
    Double getTotalEarningsByMentorIdAndDateRange(
            @Param("mentorId") Integer mentorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<SessionEntity> findByMentor_MentorId(Integer mentorId);
}

