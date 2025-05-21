package com.gina.simulator.exception;

/**
 * Default exception for endpoints.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
