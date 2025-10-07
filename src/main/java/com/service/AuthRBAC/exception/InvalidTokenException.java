package com.service.AuthRBAC.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super("token exception: " + message);
    }
}
