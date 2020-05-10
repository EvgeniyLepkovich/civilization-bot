package com.civilization.exception;

public class UserNotConnectedToSteamException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.USER_NOT_CONNECTED_TO_STEAM;

    public UserNotConnectedToSteamException() {
        super(ExceptionMessageFactory.getMessage(code));
    }

    public UserNotConnectedToSteamException(Throwable cause) {
        super(ExceptionMessageFactory.getMessage(code), cause);
    }
}
