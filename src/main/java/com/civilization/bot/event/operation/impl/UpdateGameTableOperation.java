package com.civilization.bot.event.operation.impl;

import com.civilization.bot.DiscordBot;
import com.civilization.dto.LobbyDto;
import com.civilization.service.LobbyService;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UpdateGameTableOperation {

    @Autowired
    private LobbyService lobbyService;

    @Value("${discord.channel.rating-games.channel-id}")
    private Long ratingGamesChannelId;

    public void updateGameMessage(String newGameTable, Long gameId) throws RateLimitedException {
        LobbyDto lobby = lobbyService.getLobbyByGameId(gameId);
        DiscordBot.getInstance().getBotInstance().getTextChannelById(ratingGamesChannelId)
                .editMessageById(lobby.getLobbyMessageId(), "```" + newGameTable + "```").queue();
    }
}
