package com.skill_mentor.root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SkillMentorRootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillMentorRootApplication.class, args);
	}

}

// To-Do List

// student - studentId, user, college, major, (done)
// mentor (done)
// classRoom (done)
// session sessionId classRoomId studentId mentorId startTime endTime topic link fee status (done)
// role based auth (done)
// swagger (done)
// logs (done)
// actuator
// mentor payment endpoint (need to test) ---- only session rows with status FINISHED (done)
// redis (done)

