package com.zorvyn.mappers;

import com.zorvyn.dto.request.UserRequestDto;
import com.zorvyn.dto.response.UserResponseDto;
import com.zorvyn.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getRole(), user.getEmail(),user.isActive());
    }
    public User toEntity(UserRequestDto user) {
        User userEntity = new User();
        userEntity.setName(user.name());
        userEntity.setEmail(user.email());
        userEntity.setRole(user.role());
        return userEntity;
    }
}
