package com.skill_mentor.root.service;

import com.skill_mentor.root.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers(String role, Boolean isActive);

    UserDTO getUserById(Integer id);

    UserDTO updateUserById(Integer id, UserDTO userDTO);

    boolean deleteUserById(Integer id);
}
