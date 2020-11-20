package com.civilization.bot.event;

import com.civilization.configuration.custom.DiscordMessageListenerQualifier;
import com.civilization.service.NotificationSenderService;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DiscordMessageListenerQualifier
public class UserJoinGuildListener extends ListenerAdapter {

    private static final String NEW_USER_JOIN_MESSAGE =
            "Добро пожаловать на сервер! Для рейтинговых игр CivilizationV у вас должен быть привязан акк Steam к discord, " +
            "количество наигранных часов не меньше 500, либо за вас должен кто-то поручиться. Профиль Steam на момент проверки модератором должен быть открыт. " +
            "Никнейм не должен содержать пробелов";

    @Autowired
    private NotificationSenderService notificationSenderService;

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        notificationSenderService.sendNotificationToNewUser(event.getJDA(), event.getMember().getNickname(), NEW_USER_JOIN_MESSAGE);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        System.out.println("just here");
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        System.out.println("just here");
    }
}
