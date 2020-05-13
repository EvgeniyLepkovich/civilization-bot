package com.civilization.service;

import net.dv8tion.jda.core.JDA;

public interface NotificationSenderService {
    boolean sendNotification(JDA jda, String message);
}
