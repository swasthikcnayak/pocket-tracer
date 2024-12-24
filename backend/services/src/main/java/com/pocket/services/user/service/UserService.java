package com.pocket.services.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pocket.services.user.dto.request.LoginUserDto;
import com.pocket.services.user.dto.request.RegisterUserDto;
import com.pocket.services.user.dto.response.LoginUserResponseDto;
import com.pocket.services.user.dto.response.RegisterUserResponseDto;
import com.pocket.services.user.mapper.UserMapper;
import com.pocket.services.user.model.User;
import com.pocket.services.user.repository.UserRepository;
import com.pocket.services.user.security.util.JwtUtils;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> registerUser(RegisterUserDto userDto) {
        User user = userMapper.toUserModel(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
            return ResponseEntity.ok()
                    .body(new RegisterUserResponseDto(HttpStatus.CREATED.value(), "User created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid");
        }
    }

    public ResponseEntity<?> login(LoginUserDto userDto) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        } catch (AuthenticationException exception) {
            return ResponseEntity.badRequest().body("Invalid");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        LoginUserResponseDto response = jwtUtils.generateToken(user.getUsername());
        return ResponseEntity.ok().body(response);
    }
}
