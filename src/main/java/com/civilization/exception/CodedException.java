package com.civilization.exception;

public class CodedException extends RuntimeException {

    public CodedException(ExceptionCode code) {
        super(ExceptionMessageFactory.getMessage(code));
    }

    public CodedException(ExceptionCode code, Throwable cause) {
        super(ExceptionMessageFactory.getMessage(code), cause);
    }
}
