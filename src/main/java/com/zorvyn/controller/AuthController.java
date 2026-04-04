package com.zorvyn.controller;

import com.zorvyn.dto.request.LoginRequestDto;
import com.zorvyn.dto.request.UserRequestDto;
import com.zorvyn.dto.response.AuthResponseDto;
import com.zorvyn.dto.response.UserResponseDto;
import com.zorvyn.model.User;
import com.zorvyn.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto user) {
        UserResponseDto userResponseDto= authService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginUser) {
        AuthResponseDto auth = authService.login(loginUser);
        return ResponseEntity.ok(auth);
    }

}
