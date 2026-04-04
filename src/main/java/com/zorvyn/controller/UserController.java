package com.zorvyn.controller;

import com.zorvyn.dto.response.UserResponseDto;
import com.zorvyn.model.enums.Role;
import com.zorvyn.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name = "Users", description = "User management - Admin only")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users")
    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user role")
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponseDto>updateUserRole(@PathVariable UUID id, @RequestParam Role role){
        return ResponseEntity.ok(userService.updateUserRole(id,role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id){
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate user")
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable UUID id){
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }
}
