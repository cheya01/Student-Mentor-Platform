package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.LoginRequestDTO;
import com.skill_mentor.root.dto.UserDTO;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.repository.UserRepository;
import com.skill_mentor.root.service.JwtService;
import com.skill_mentor.root.service.UserService;
import com.skill_mentor.root.service.impl.ClassRoomServiceimpl;
import com.skill_mentor.root.validation.OnCreate;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    public AuthController(UserRepository userRepository, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        logger.debug("Attempt login for email: {}", loginRequest.getEmail());

        try {
            UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

            String token = jwtService.generateToken(user);
            return ResponseEntity.ok().body(Map.of("token", token, "role", user.getRole().getRole()));

        } catch (UsernameNotFoundException e) {
            logger.warn("Login failed for email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error during login for email: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }



    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> createUser(@Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }
}
