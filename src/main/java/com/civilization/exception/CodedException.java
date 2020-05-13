package com.civilization.exception;

public class CodedException extends RuntimeException {

    public CodedException(ExceptionCode code) {
        super(ExceptionMessageFactory.getMessage(code));
    }

    public CodedException(ExceptionCode code, Throwable cause) {
        super(ExceptionMessageFactory.getMessage(code), cause);
    }

    public CodedException(ExceptionCode code, String externalMessage) {
        super(ExceptionMessageFactory.getMessage(code) + ": " + externalMessage);
    }

    public CodedException(ExceptionCode code, String externalMessage, Throwable cause) {
        super(ExceptionMessageFactory.getMessage(code) + ": " + externalMessage, cause);
    }
}
