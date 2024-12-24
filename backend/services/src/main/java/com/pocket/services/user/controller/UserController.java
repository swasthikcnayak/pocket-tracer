package com.pocket.services.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.pocket.services.user.dto.request.LoginUserDto;
import com.pocket.services.user.dto.request.RegisterUserDto;
import com.pocket.services.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto userDto) {
        return userService.login(userDto);
    }
}
