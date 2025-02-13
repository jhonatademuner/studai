package com.studai.utils.assembler;

import com.studai.domain.user.User;
import com.studai.domain.user.dto.UserDTO;

import java.util.UUID;

public class UserAssembler {

    public static User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId() != null ? UUID.fromString(dto.getId()) : null)
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId() != null ? user.getId().toString() : null)
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}