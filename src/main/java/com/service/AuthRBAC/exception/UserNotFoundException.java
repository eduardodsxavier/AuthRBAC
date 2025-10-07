package com.service.AuthRBAC.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("user not found " + message);
    }
}
