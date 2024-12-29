package com.pocket.services.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pocket.services.common.exceptions.UnhandledException;
import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.common.security.util.JwtUtils;
import com.pocket.services.common.user.model.User;
import com.pocket.services.common.user.repository.UserRepository;
import com.pocket.services.user.dto.mapper.UserMapper;
import com.pocket.services.user.dto.request.LoginUserDto;
import com.pocket.services.user.dto.request.RegisterUserDto;
import com.pocket.services.user.dto.response.LoginUserResponseDto;
import com.pocket.services.user.exceptions.ErrorCode;
import com.pocket.services.user.exceptions.UserServiceException;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

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
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        } catch (DataIntegrityViolationException ex) {
            throw new UserServiceException(ErrorCode.USER_CONSTRAINS_DOES_NOT_MATCH, "User Already exist");
        } catch (JpaSystemException ex) {
            throw new UserServiceException(ErrorCode.USER_ALREADY_EXIST, "User Already exist");
        } catch (Exception e) {
            logger.error("Exception in register User", e);
            throw new UnhandledException(ErrorCode.USER_CREATION_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> login(LoginUserDto userDto) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        } catch (AuthenticationException exception) {
            logger.error("Authentication exception in login User", exception);
            throw new UserServiceException(ErrorCode.USER_LOGIN_EXCEPTION, "Bad Credentials", HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserInfo user = (UserInfo) authentication.getPrincipal();
        LoginUserResponseDto response = jwtUtils.generateToken(user.getUsername());
        return ResponseEntity.ok().body(response);
    }
}
