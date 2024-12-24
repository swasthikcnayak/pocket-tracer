package com.pocket.services.user.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginUserResponseDto {
    String userName;
    String token;
    Date expiry;
}
