package com.zorvyn.dto.request;

public record LoginRequestDto(
        String email,
        String password
) {
}
