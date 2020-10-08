package com.civilization.exception;

public class GameNotFoundException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.GAME_NOT_FOUND;

    public GameNotFoundException() {
        super(code);
    }

    public GameNotFoundException(ExceptionCode code, Throwable cause) {
        super(code, cause);
    }

    public GameNotFoundException(ExceptionCode code, String externalMessage) {
        super(code, externalMessage);
    }

    public GameNotFoundException(ExceptionCode code, String externalMessage, Throwable cause) {
        super(code, externalMessage, cause);
    }
}
