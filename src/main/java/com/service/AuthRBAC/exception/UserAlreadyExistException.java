package com.service.AuthRBAC.exception;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String username) {
        super(username + " is already in use");
    }
}
