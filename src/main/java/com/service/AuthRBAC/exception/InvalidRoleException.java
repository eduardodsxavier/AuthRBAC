package com.service.AuthRBAC.exception;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException(String role) {
        super("invalid role " + role);
    }
    
}
