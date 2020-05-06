package com.civilization.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionMessageFactory {

    private ExceptionMessageFactory() {}

    public static final Map<ExceptionCode, String> exceptions = new HashMap<ExceptionCode, String>() {{
        put(ExceptionCode.USER_NOT_CONNECTED_TO_STEAM, "Some of users is not connected to steam. Please contact administrator.");
    }};

    public static String getMessage(ExceptionCode code) {
        return exceptions.get(code);
    }
}
