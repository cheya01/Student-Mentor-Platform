package com.skill_mentor.root.controller;

import com.skill_mentor.root.dto.LoginRequestDTO;
import com.skill_mentor.root.dto.UserDTO;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.repository.UserRepository;
import com.skill_mentor.root.service.JwtService;
import com.skill_mentor.root.service.UserService;
import com.skill_mentor.root.validation.OnCreate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public AuthController(UserRepository userRepository, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok().body(Map.of("token", token, "role", user.getRole().getRole()));
    }


    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> createUser(@Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }
}
