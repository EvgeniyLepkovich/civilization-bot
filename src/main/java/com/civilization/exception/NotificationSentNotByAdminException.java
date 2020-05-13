package com.civilization.exception;

public class NotificationSentNotByAdminException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.NOTIFICATION_SENT_NOT_BY_ADMIN;

    public NotificationSentNotByAdminException() {
        super(ExceptionMessageFactory.getMessage(code));
    }

    public NotificationSentNotByAdminException(Throwable cause) {
        super(ExceptionMessageFactory.getMessage(code), cause);
    }
}
