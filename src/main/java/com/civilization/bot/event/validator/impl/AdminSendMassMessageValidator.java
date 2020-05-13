package com.civilization.bot.event.validator.impl;

import com.civilization.bot.event.validator.Validator;
import com.civilization.exception.NotificationSentNotByAdminException;
import com.civilization.exception.WrongChannelNameForNotificationsException;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("adminSendMassMessageValidator")
public class AdminSendMassMessageValidator implements Validator {

    @Value("${discord.channel.notification.name}")
    private String expectedChannelName;
    @Value("${discord.administrator.role.name}")
    private String adminRoleName;

    @Override
    public void validate(Object object) {
        MessageReceivedEvent event = (MessageReceivedEvent) object;

        checkOwnerIsAdmin(event);
        checkChannelIsCorrect(event);
    }

    private void checkChannelIsCorrect(MessageReceivedEvent event) {
        String channelName = event.getChannel().getName();
        if (!expectedChannelName.equals(channelName)) {
            throw new WrongChannelNameForNotificationsException();
        }
    }

    private void checkOwnerIsAdmin(MessageReceivedEvent event) {
        List<Role> roles = event.getGuild().getMember(event.getAuthor()).getRoles();
        List<String> stringRoles = toStringRoles(roles);
        if (!stringRoles.contains(adminRoleName)) {
            throw new NotificationSentNotByAdminException();
        }
    }

    private List<String> toStringRoles(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
