package com.zorvyn.dto.response;

import com.zorvyn.model.enums.Role;

import java.util.UUID;

public record UserResponseDto (
    UUID id,
     String name,
     Role role,
     String email,
     Boolean isActive
){}
