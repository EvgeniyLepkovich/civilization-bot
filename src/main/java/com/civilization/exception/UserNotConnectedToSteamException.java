package com.civilization.exception;

public class UserNotConnectedToSteamException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.USER_NOT_CONNECTED_TO_STEAM;

    public UserNotConnectedToSteamException() {
        super(code);
    }

    public UserNotConnectedToSteamException(Throwable cause) {
        super(code, cause);
    }
}
