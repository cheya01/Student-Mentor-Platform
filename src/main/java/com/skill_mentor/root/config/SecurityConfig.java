package com.skill_mentor.root.config;

import com.skill_mentor.root.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Swagger API docs is public
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll() // login and sign-up is public

                        .requestMatchers("/api/v1/user/**").hasRole("ADMIN") // only admin can manage users

                        .requestMatchers("/api/v1/session").hasAnyRole("ADMIN", "MENTOR") // only admin and mentor can CRUD sessions
                        .requestMatchers("/api/v1/session/end/**").hasAnyRole("ADMIN", "MENTOR") // only admin and mentor can end sessions

                        .requestMatchers(HttpMethod.POST, "/api/v1/classroom").hasRole("ADMIN") // only admin can manage classrooms
                        .requestMatchers("/api/v1/classroom/**").hasRole("ADMIN") // only admin can manage classrooms

                        .requestMatchers(HttpMethod.POST,"/api/v1/student").hasAnyRole("ADMIN", "STUDENT") // only admin and student can create students
                        .requestMatchers(HttpMethod.GET,"/api/v1/student").hasRole("ADMIN") // only admin can get all students
                        .requestMatchers(HttpMethod.GET,"/api/v1/student/**").hasAnyRole("ADMIN", "STUDENT") // only admin can get student by id
                        .requestMatchers(HttpMethod.PUT,"/api/v1/student/**").hasAnyRole("ADMIN", "STUDENT") // only admin and student can update student by id
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/student/**").hasRole("ADMIN") // only admin can delete student

                        .requestMatchers(HttpMethod.POST,"/api/v1/mentor").hasAnyRole("ADMIN", "MENTOR") // only admin and mentor can create mentors
                        .requestMatchers(HttpMethod.GET,"/api/v1/mentor").hasRole("ADMIN") // only admin can get all mentors
                        .requestMatchers(HttpMethod.GET,"/api/v1/mentor/**").hasRole("ADMIN") // only admin can get mentor by id
                        .requestMatchers(HttpMethod.PUT,"/api/v1/mentor/**").hasAnyRole("ADMIN", "MENTOR") // only admin and mentor can update mentor by id
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/mentor/**").hasRole("ADMIN") // only admin can delete mentor

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Strength 10 by default
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
