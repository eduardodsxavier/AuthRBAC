package com.service.AuthRBAC.exception;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException(String message) {
        super("invalid role " + message);
    }
    
}
