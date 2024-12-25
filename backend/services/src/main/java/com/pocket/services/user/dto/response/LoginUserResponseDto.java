package com.pocket.services.user.dto.response;

import java.time.Instant;

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
    Instant expiry;
}
