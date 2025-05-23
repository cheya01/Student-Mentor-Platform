package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.UserDTO;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.mapper.UserEntityDTOMapper;
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


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // Hash the plain password before mapping
        String hashedPassword = passwordEncoder.encode(userDTO.getPasswordHash());
        userDTO.setPasswordHash(hashedPassword);

        UserEntity userEntity = UserEntityDTOMapper.map(userDTO);
        UserEntity savedEntity = userRepository.save(userEntity);
        return UserEntityDTOMapper.map(savedEntity);
    }

    @Override
    public List<UserDTO> getAllUsers(String role, Boolean isActive) {
        List<UserEntity> userEntities = userRepository.findAll();

        return userEntities.stream()
                .filter(user -> {
                    boolean matchesRole = (role == null ||
                            user.getRole() != null &&
                                    user.getRole().equalsIgnoreCase(role));

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
                    existing.setPasswordHash(userDTO.getPasswordHash());
                    existing.setRole(userDTO.getRole());
                    existing.setIsActive(userDTO.getIsActive());
                    existing.setPhoneNumber(userDTO.getPhoneNumber());
                    existing.setAddress(userDTO.getAddress());
                    existing.setNIC(userDTO.getNIC());
                    return UserEntityDTOMapper.map(userRepository.save(existing));
                }).orElse(null);
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
