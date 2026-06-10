package com.tubenotify.tubenotify_backend.common.exception;


/**
 * Exception thrown when invalid credentials are provided during authentication
 */
public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}