package com.service.AuthRBAC.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserAlreadyExistAdvice {

    @ExceptionHandler(UserAlreadyExistException.class) 
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userAlreadyExistHandler(UserAlreadyExistException exception) {
        return exception.getMessage();
    }
}
