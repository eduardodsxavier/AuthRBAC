package com.service.AuthRBAC.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.http.HttpServletRequest;

import com.service.AuthRBAC.security.UserDetailsImpl;

@Service
public class JwtService {

    private static final String SECRET_KEY = "change to enviroment variable"; 
    private static final String ISSUER = "AUTHRBAC"; 

    public String generateToken(UserDetailsImpl user) {
        return JWT.create()
            .withIssuer(ISSUER)
            .withIssuedAt(creationDate())
            .withExpiresAt(expirationDate())
            .withSubject(user.getUsername())
            .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String getSubjectFromToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("invalid or expired token");
        }
    }
    
    private Instant creationDate() {
        return ZonedDateTime.now(ZoneId.of("GMT-3")).toInstant();
    }

    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("GMT-3")).plusMinutes(15).toInstant();
    }

    public String recoveryToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) {return null;}
        return header.replace("Bearer ", "");
    }
}
