package com.zorvyn.service;

import com.zorvyn.dto.response.UserResponseDto;
import com.zorvyn.exception.ResourceNotFoundException;
import com.zorvyn.mappers.UserMapper;
import com.zorvyn.model.User;
import com.zorvyn.model.enums.Role;
import com.zorvyn.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    //for admin

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll().stream().map(userMapper::toResponseDto).toList();
    }
    public UserResponseDto getUserById(UUID id){
        return userMapper.toResponseDto(userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found")));
    }
    public UserResponseDto updateUserRole(UUID id, Role role){
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        user.setRole(role);
        return userMapper.toResponseDto(userRepository.save(user));
    }
    public void deactivateUser(UUID id){
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }
    public void activateUser(UUID id){
        User user=userRepository.findById(id).orElseThrow();
        user.setActive(true);
        userRepository.save(user);
    }


}
