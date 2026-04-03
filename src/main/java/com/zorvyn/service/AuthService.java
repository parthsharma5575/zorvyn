package com.zorvyn.service;

import com.zorvyn.dto.request.LoginRequestDto;
import com.zorvyn.dto.request.UserRequestDto;
import com.zorvyn.dto.response.AuthResponseDto;
import com.zorvyn.dto.response.UserResponseDto;
import com.zorvyn.exception.ResourceAlreadyExistsException;
import com.zorvyn.mappers.UserMapper;
import com.zorvyn.model.User;
import com.zorvyn.repository.UserRepository;
import com.zorvyn.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public AuthService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public UserResponseDto register(UserRequestDto user) {
        if(userRepository.existsByEmail(user.email())){
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        User userEntity = userMapper.toEntity(user);
        String encodedPassword=bCryptPasswordEncoder.encode(user.password());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
        return userMapper.toResponseDto(userEntity);

    }
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password()));
                User user=userRepository.findByEmail(loginRequestDto.email()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
               String token= jwtUtil.generateToken(user);
            return new AuthResponseDto(token,user.getEmail(),user.getRole());

    }
}
