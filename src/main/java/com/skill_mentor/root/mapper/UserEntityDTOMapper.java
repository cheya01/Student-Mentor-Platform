package com.skill_mentor.root.mapper;

import com.skill_mentor.root.dto.UserDTO;
import com.skill_mentor.root.entity.UserEntity;

public class UserEntityDTOMapper {

    public static UserDTO map(UserEntity userEntity) {
        if (userEntity == null) return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPasswordHash(userEntity.getPasswordHash());
        userDTO.setRole(userEntity.getRole());
        userDTO.setIsActive(userEntity.getIsActive());
        userDTO.setCreatedAt(userEntity.getCreatedAt());
        userDTO.setLastLogin(userEntity.getLastLogin());
        userDTO.setPhoneNumber(userEntity.getPhoneNumber());
        userDTO.setAddress(userEntity.getAddress());
        userDTO.setNIC(userEntity.getNIC());
        return userDTO;
    }

    public static UserEntity map(UserDTO userDTO) {
        if (userDTO == null) return null;

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPasswordHash(userDTO.getPasswordHash());
        userEntity.setRole(userDTO.getRole());
        userEntity.setIsActive(userDTO.getIsActive());
        userEntity.setCreatedAt(userDTO.getCreatedAt());
        userEntity.setLastLogin(userDTO.getLastLogin());
        userEntity.setPhoneNumber(userDTO.getPhoneNumber());
        userEntity.setAddress(userDTO.getAddress());
        userEntity.setNIC(userDTO.getNIC());
        return userEntity;
    }
}
