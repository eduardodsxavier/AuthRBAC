package com.service.AuthRBAC.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.JWTVerificationException;


@RestControllerAdvice
public class JWTVerificationAdvice {

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String jwtVerificationHandler(JWTVerificationException exception) {
        return exception.getMessage();
    }
}
