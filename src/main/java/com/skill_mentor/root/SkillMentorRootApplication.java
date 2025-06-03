package com.skill_mentor.root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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
// role based auth
// swagger
// logs
// actuator

