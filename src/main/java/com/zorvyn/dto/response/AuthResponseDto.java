package com.zorvyn.dto.response;

public record AuthResponseDto(
        String token,
        String email,
        com.zorvyn.model.enums.Role role
) {
}
