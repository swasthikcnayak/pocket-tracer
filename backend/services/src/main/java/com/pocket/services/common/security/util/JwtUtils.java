package com.pocket.services.common.security.util;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pocket.services.user.dto.response.LoginUserResponseDto;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

    @Value("${spring.app.jwt-secret}")
    private String jwtSecret;

    @Value("${spring.app.jwt-expiration-millis}")
    private long jwtExpirationMs;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public LoginUserResponseDto generateToken(String email) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtExpirationMs, ChronoUnit.MILLIS);
        String jwtString = Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key())
                .compact();
        return LoginUserResponseDto.builder().userName(email).expiry(expiry).token(jwtString).build();
    }

    public String getUserNameFromJwt(String token) {
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload()
                .getSubject();

    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {

        } catch (ExpiredJwtException e) {

        } catch (UnsupportedJwtException e) {

        } catch (IllegalArgumentException e) {

        }
        return false;
    }

}
