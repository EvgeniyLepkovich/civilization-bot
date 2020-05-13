package com.civilization.exception;

public class ChannelNotSupportedForEventException extends CodedException {
    private static final ExceptionCode code = ExceptionCode.CHANNEL_NOT_SUPPORTED_FOR_EVENT;

    public ChannelNotSupportedForEventException() {
        super(code);
    }

    public ChannelNotSupportedForEventException(Throwable cause) {
        super(code, cause);
    }

    public ChannelNotSupportedForEventException(String externalMessage) {
        super(code, externalMessage);
    }

    public ChannelNotSupportedForEventException(String externalMessage, Throwable cause) {
        super(code, externalMessage, cause);
    }
}
