package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.UserDTO;
import com.skill_mentor.root.entity.RoleEntity;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.mapper.UserEntityDTOMapper;
import com.skill_mentor.root.repository.RoleRepository;
import com.skill_mentor.root.repository.UserRepository;
import com.skill_mentor.root.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        String hashedPassword = passwordEncoder.encode(userDTO.getPasswordHash());
        userDTO.setPasswordHash(hashedPassword);

        RoleEntity role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));

        UserEntity userEntity = UserEntityDTOMapper.map(userDTO, role);
        UserEntity savedEntity = userRepository.save(userEntity);

        return UserEntityDTOMapper.map(savedEntity);
    }

    @Override
    public List<UserDTO> getAllUsers(String roleName, Boolean isActive) {
        List<UserEntity> userEntities = userRepository.findAll();

        return userEntities.stream()
                .filter(user -> {
                    boolean matchesRole = (roleName == null ||
                            user.getRole() != null &&
                                    roleName.equalsIgnoreCase(user.getRole().getRole()));

                    boolean matchesActive = (isActive == null || isActive.equals(user.getIsActive()));

                    return matchesRole && matchesActive;
                })
                .map(UserEntityDTOMapper::map)
                .toList();
    }


    @Override
    public UserDTO getUserById(Integer id) {
        return userRepository.findById(id)
                .map(UserEntityDTOMapper::map)
                .orElse(null);
    }

    @Override
    public UserDTO updateUserById(Integer id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(userDTO.getFirstName());
                    existing.setLastName(userDTO.getLastName());
                    existing.setEmail(userDTO.getEmail());

                    // Hash the password only if a new one is provided
                    if (userDTO.getPasswordHash() != null && !userDTO.getPasswordHash().isBlank()) {
                        existing.setPasswordHash(passwordEncoder.encode(userDTO.getPasswordHash()));
                    }

                    // Resolve role from roleId
                    RoleEntity role = roleRepository.findById(userDTO.getRoleId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));
                    existing.setRole(role);

                    existing.setIsActive(userDTO.getIsActive());
                    existing.setPhoneNumber(userDTO.getPhoneNumber());
                    existing.setAddress(userDTO.getAddress());
                    existing.setNIC(userDTO.getNIC());

                    return UserEntityDTOMapper.map(userRepository.save(existing));
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }


    @Override
    public boolean deleteUserById(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
