package com.civilization.exception;

public class NoAvailableChannelsForEventException extends CodedException {

    private static final ExceptionCode code = ExceptionCode.NO_AVAILABLE_CHANNELS_FOR_EVENT;

    public NoAvailableChannelsForEventException() {
        super(code);
    }

    public NoAvailableChannelsForEventException(Throwable cause) {
        super(code, cause);
    }

    public NoAvailableChannelsForEventException(String externalMessage) {
        super(code, externalMessage);
    }

    public NoAvailableChannelsForEventException(String externalMessage, Throwable cause) {
        super(code, externalMessage, cause);
    }
}
