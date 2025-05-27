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
        List<UserEntity> userEntities = userRepository.findAllWithRoles(); // fetch roles eagerly

        return userEntities.stream()
                .filter(user -> {
                    boolean matchesRole = (roleName == null || (
                            user.getRole() != null &&
                                    user.getRole().getRole() != null &&
                                    roleName.equalsIgnoreCase(user.getRole().getRole())
                    ));

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
                .map(user -> {
                    if (userDTO.getFirstName() != null)
                        user.setFirstName(userDTO.getFirstName());
                    if (userDTO.getLastName() != null)
                        user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null)
                        user.setEmail(userDTO.getEmail());
                    if (userDTO.getPasswordHash() != null && !userDTO.getPasswordHash().isBlank())
                        user.setPasswordHash(passwordEncoder.encode(userDTO.getPasswordHash()));
                    if (userDTO.getRoleId() != null)
                        user.setRole(roleRepository.findById(userDTO.getRoleId()).orElseThrow());
                    if (userDTO.getPhoneNumber() != null)
                        user.setPhoneNumber(userDTO.getPhoneNumber());
                    if (userDTO.getAddress() != null)
                        user.setAddress(userDTO.getAddress());
                    if (userDTO.getNIC() != null)
                        user.setNIC(userDTO.getNIC());
                    if (userDTO.getIsActive() != null)
                        user.setIsActive(userDTO.getIsActive());

                    return UserEntityDTOMapper.map(userRepository.save(user));
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
