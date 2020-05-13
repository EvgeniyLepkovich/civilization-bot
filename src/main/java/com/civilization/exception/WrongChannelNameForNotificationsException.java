package com.civilization.exception;

public class WrongChannelNameForNotificationsException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.WRONG_CHANNEL_NAME_FOR_NOTIFICATION;

    public WrongChannelNameForNotificationsException() {
        super(ExceptionMessageFactory.getMessage(code));
    }

    public WrongChannelNameForNotificationsException(Throwable cause) {
        super(ExceptionMessageFactory.getMessage(code), cause);
    }
}
