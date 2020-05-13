package com.civilization.exception;

public class UsersForNotificationNotFoundException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.USERS_FOR_NOTIFICATION_NOT_FOUND;

    public UsersForNotificationNotFoundException() {
        super(code);
    }

    public UsersForNotificationNotFoundException(Throwable cause) {
        super(code, cause);
    }
}
