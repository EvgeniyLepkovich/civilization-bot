package com.civilization.exception;

public enum ExceptionCode {
    USER_NOT_CONNECTED_TO_STEAM("SNC");

    String code;

    ExceptionCode(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
