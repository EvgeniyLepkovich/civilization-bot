package com.civilization.exception;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionMessageFactory {

    private ExceptionMessageFactory() {}

    public static final Map<ExceptionCode, String> exceptions = new HashMap<ExceptionCode, String>() {{
        put(ExceptionCode.USER_NOT_CONNECTED_TO_STEAM, "Some of users is not connected to steam. Please contact administrator.");
        put(ExceptionCode.WRONG_CHANNEL_NAME_FOR_NOTIFICATION, "Wrong channel for notifications, please use the correct one");
        put(ExceptionCode.NOTIFICATION_SENT_NOT_BY_ADMIN, "Notification for all users could be sent only by admin");
        put(ExceptionCode.USERS_FOR_NOTIFICATION_NOT_FOUND, "Could not receive the list of users to send the message");
        put(ExceptionCode.NOTIFICATION_MESSAGE_IS_BLANK, "Notification message is blank, please put something after !send");
        put(ExceptionCode.NO_AVAILABLE_CHANNELS_FOR_EVENT, "No available channels for event");
        put(ExceptionCode.CHANNEL_NOT_SUPPORTED_FOR_EVENT, "Channel not supported for event");
        put(ExceptionCode.NOT_ENOUGH_PERMISSIONS, "You don't have enough permission to execute operation");
        put(ExceptionCode.GAME_NOT_FOUND, "Can't find the game");
        put(ExceptionCode.GAME_NOT_STARTED, "Game not started");
    }};

    public static String getMessage(ExceptionCode code) {
        return exceptions.get(code);
    }
}
