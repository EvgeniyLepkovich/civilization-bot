package com.civilization.discord;

import com.civilization.bot.DiscordBot;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Value("${discord.channel.rating-games.channel-id}")
    private Long ratingGamesChannelId;

    public void sendMessageToRatingGameChannel(String message) {
        TextChannel textChannel = DiscordBot.getInstance().getBotInstance().getTextChannelById(ratingGamesChannelId);
        textChannel.sendMessage(message).queue();
    }
}
