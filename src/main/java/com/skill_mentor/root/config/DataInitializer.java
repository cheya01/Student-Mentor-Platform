package com.skill_mentor.root.config;

import com.skill_mentor.root.entity.RoleEntity;
import com.skill_mentor.root.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new RoleEntity(1, "STUDENT"));
                roleRepository.save(new RoleEntity(2, "MENTOR"));
                roleRepository.save(new RoleEntity(3, "VISITOR"));
                roleRepository.save(new RoleEntity(4, "ADMIN"));
            }
        };
    }
}
