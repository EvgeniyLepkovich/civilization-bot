package com.civilization.exception;

public class GameNotStartedException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.GAME_NOT_STARTED;

    public GameNotStartedException() {
        super(code);
    }

    public GameNotStartedException(ExceptionCode code, Throwable cause) {
        super(code, cause);
    }

    public GameNotStartedException(ExceptionCode code, String externalMessage) {
        super(code, externalMessage);
    }

    public GameNotStartedException(ExceptionCode code, String externalMessage, Throwable cause) {
        super(code, externalMessage, cause);
    }
}
