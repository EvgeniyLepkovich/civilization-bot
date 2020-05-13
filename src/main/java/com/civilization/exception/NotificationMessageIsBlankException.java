package com.civilization.exception;

public class NotificationMessageIsBlankException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.NOTIFICATION_MESSAGE_IS_BLANK;

    public NotificationMessageIsBlankException() {
        super(ExceptionMessageFactory.getMessage(code));
    }

    public NotificationMessageIsBlankException(Throwable cause) {
        super(ExceptionMessageFactory.getMessage(code), cause);
    }
}
