package com.civilization.exception;

public class CodedException extends RuntimeException {

    public CodedException(String message) {
        super(message);
    }

    public CodedException(String message, Throwable cause) {
        super(message, cause);
    }
}
