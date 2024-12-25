package com.pocket.services.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginUserDto {
    
    @NotNull(message = "Email cannot be null")
    String email;

    @NotNull(message = "Password cannot be null")
    String password;
}
