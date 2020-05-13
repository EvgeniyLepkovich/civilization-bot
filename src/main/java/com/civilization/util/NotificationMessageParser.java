package com.civilization.util;

public class NotificationMessageParser {

    private static final String SEND_FLAG = "!send";

    public static String parseMessage(String message) {
        return message.substring(SEND_FLAG.length() + 1);
    }
}
