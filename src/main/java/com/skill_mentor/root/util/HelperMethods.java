package com.skill_mentor.root.util;

import com.skill_mentor.root.entity.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;

public class HelperMethods {

    public static UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserEntity user) {
            return user;
        }
        throw new RuntimeException("Invalid user in security context");
    }
}
