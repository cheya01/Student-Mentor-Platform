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
// actuator (later)
// mentor payment endpoint (need to test) ---- only session rows with status FINISHED (done)
// redis (done)

// check is_active when login (done)
// role based authorization (done)
// user-identity-based ownership checks (assumption - a user has only one role) (done)
// list sessions by mentor id (done)

// password validation (done)
// https (done)
// user age and gender (done)
// make NIC unique (done)
// clear error res on sig-up and login (done)
// supabase migration (done)

// list sessions by student id (done)
// jason property seen eka hadanna (done)
// last login case (done)

// learn CI/CD on hex coder
// render deployment with CI/CD
// test commit
// sonar cube (done)
// jenkins
// veracode

// google auth (not possible without a front end)

