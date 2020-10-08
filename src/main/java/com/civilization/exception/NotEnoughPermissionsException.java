package com.civilization.exception;

public class NotEnoughPermissionsException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.NOT_ENOUGH_PERMISSIONS;

    public NotEnoughPermissionsException() {
        super(code);
    }

    public NotEnoughPermissionsException(ExceptionCode code, Throwable cause) {
        super(code, cause);
    }

    public NotEnoughPermissionsException(ExceptionCode code, String externalMessage) {
        super(code, externalMessage);
    }

    public NotEnoughPermissionsException(ExceptionCode code, String externalMessage, Throwable cause) {
        super(code, externalMessage, cause);
    }
}
