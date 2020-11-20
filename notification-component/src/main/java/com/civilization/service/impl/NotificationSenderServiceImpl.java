package com.civilization.service.impl;

import com.civilization.service.NotificationSenderService;
import com.civilization.util.NotificationMessageParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationSenderServiceImpl implements NotificationSenderService {

    @Override
    public boolean sendNotification(JDA jda, String message) {
        //TODO: ask about roles here
        //get all recipients --> by role "specific role" -> recipients (validate)
        String messageToSend = getMessageToSend(message);
        sendMessageToAllUsers(jda, messageToSend);

        return true;
    }

    @Override
    public void sendNotificationToNewUser(JDA jda, User user, String message) {
        user.openPrivateChannel().queue(q -> q.sendMessage(message).queue());
    }

    private String getMessageToSend(String message) {
        String messageToSend = NotificationMessageParser.parseMessage(message);
        checkMessageToSend(messageToSend);
        return messageToSend;
    }

    private void sendMessageToAllUsers(JDA jda, String messageToSend) {
        List<User> users = getUsersToSendNotification(jda);
        users.forEach(user -> user.openPrivateChannel().queue(q -> q.sendMessage(messageToSend).queue()));
    }

    private List<User> getUsersToSendNotification(JDA jda) {
        List<User> userWithoutSelf = getUsersWithoutSelf(jda);
        checkUsers(userWithoutSelf);
        return userWithoutSelf;
    }

    private List<User> getUsersWithoutSelf(JDA jda) {
        return jda.getUsers().stream()
                    .filter(user -> !user.equals(jda.getSelfUser()))
                    .collect(Collectors.toList());
    }

    private void checkUsers(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            throw new RuntimeException();
        }
    }

    private void checkMessageToSend(String messageToSend) {
        if (StringUtils.isBlank(messageToSend)) {
            throw new RuntimeException();
        }
    }
}
