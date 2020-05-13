package com.civilization.configuration.custom;

import com.civilization.configuration.custom.annotation.NotificationChannelEventAnnotation;
import com.civilization.configuration.custom.annotation.RatingGamesChannelEventAnnotation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DiscordChannelsConfiguration {

    @Bean("channelsByAnnotation")
    public Map<Class, String> channelsByAnnotation(
            @Value("${discord.channel.rating-games.name}") String ratingGamesChannels,
            @Value("${discord.channel.notification.name}") String notificationChannels) {
        HashMap<Class, String> channelsByAnnotation = new HashMap<>();
        channelsByAnnotation.put(RatingGamesChannelEventAnnotation.class, ratingGamesChannels);
        channelsByAnnotation.put(NotificationChannelEventAnnotation.class, notificationChannels);
        return channelsByAnnotation;
    }
}
