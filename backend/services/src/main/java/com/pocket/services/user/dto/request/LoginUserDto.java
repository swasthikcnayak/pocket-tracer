package com.pocket.services.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginUserDto {
    
    @NotNull(message = "email cannot be null")
    String email;

    @NotNull(message = "password cannot be null")
    String password;
}
