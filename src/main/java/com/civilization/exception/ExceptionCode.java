package com.civilization.exception;

public enum ExceptionCode {
    USER_NOT_CONNECTED_TO_STEAM("SNC"),
    WRONG_CHANNEL_NAME_FOR_NOTIFICATION("WCNFN"),
    NOTIFICATION_SENT_NOT_BY_ADMIN("NSNBA"),
    USERS_FOR_NOTIFICATION_NOT_FOUND("UFNNF"),
    NOTIFICATION_MESSAGE_IS_BLANK("NMIB"),
    NO_AVAILABLE_CHANNELS_FOR_EVENT("NACFE"),
    CHANNEL_NOT_SUPPORTED_FOR_EVENT("CNSFE");

    String code;

    ExceptionCode(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
