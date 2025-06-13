package com.skill_mentor.root.service.impl;

import com.skill_mentor.root.dto.UserDTO;
import com.skill_mentor.root.entity.RoleEntity;
import com.skill_mentor.root.entity.UserEntity;
import com.skill_mentor.root.mapper.UserEntityDTOMapper;
import com.skill_mentor.root.repository.RoleRepository;
import com.skill_mentor.root.repository.UserRepository;
import com.skill_mentor.root.service.UserService;
import com.skill_mentor.root.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceimpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceimpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserServiceimpl.class);


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating user with email: {}", userDTO.getEmail());
        try {
            // Encode password
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(hashedPassword);

            // Assign default role if none provided
            Integer roleId = userDTO.getRoleId() != null ? userDTO.getRoleId() : 3;
            if (userDTO.getRoleId() == null) {
                logger.info("No role ID provided. Assigning default role ID: {}", roleId);
            }

            RoleEntity role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid role ID: " + roleId));

            // extract dob and gender
            Map<String, Object> nicData = HelperMethods.extractDobAndGenderFromNic(userDTO.getNIC());
            userDTO.setDateOfBirth((LocalDate) nicData.get("dob"));
            userDTO.setGender((String) nicData.get("gender"));

            // Map and save user
            UserEntity userEntity = UserEntityDTOMapper.map(userDTO, role);
            UserEntity savedEntity = userRepository.save(userEntity);

            logger.info("User created successfully with ID: {}", savedEntity.getUserId());
            return UserEntityDTOMapper.map(savedEntity);

        } catch (Exception e) {
            logger.error("Error while creating user with email: {}", userDTO.getEmail(), e);
            throw e; // Let global exception handler manage this cleanly
        }
    }


    @Override
    @Cacheable(value = "usersCache")
    public List<UserDTO> getAllUsers(String roleName, Boolean isActive) {
        logger.info("Retrieving all users");
        try{
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
        } catch (Exception e) {
            logger.error("Error while retrieving users", e);
            return null;
        }
    }



    @Override
    public UserDTO getUserById(Integer id) {
        logger.info("Retrieving user with id: {}", id);
        try {
            return userRepository.findById(id)
                    .map(UserEntityDTOMapper::map)
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error while retrieving user", e);
            return null;
        }
    }

    @Override
    @CacheEvict(value = "usersCache", allEntries = true)
    public UserDTO updateUserById(Integer id, UserDTO userDTO) {
        logger.info("Updating user with id: {}", id);
        try {
            return userRepository.findById(id)
                    .map(user -> {
                        if (userDTO.getFirstName() != null)
                            user.setFirstName(userDTO.getFirstName());
                        if (userDTO.getLastName() != null)
                            user.setLastName(userDTO.getLastName());
                        if (userDTO.getEmail() != null)
                            user.setEmail(userDTO.getEmail());
                        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank())
                            user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
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
        } catch (Exception e) {
            logger.error("Error while updating user", e);
            return null;
        }
    }



    @Override
    public boolean deleteUserById(Integer id) {
        logger.info("Deleting user with id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        logger.info("User with id: {} not found", id);
        return false;
    }
}
