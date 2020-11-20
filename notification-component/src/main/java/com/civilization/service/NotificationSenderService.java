package com.civilization.service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

public interface NotificationSenderService {
    boolean sendNotification(JDA jda, String message);
    void sendNotificationToNewUser(JDA jda, User user, String message);
}
