package com.civilization.bot.event.operation.impl;

import com.civilization.cache.CreatedGameMessagesCache;
import com.civilization.model.ActiveGame;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Comparator;

@Component
public class UpdateGameTableOperation {

    public void updateGameMessage(String newGameTable, Long gameId) throws RateLimitedException {
        CreatedGameMessagesCache createdGameMessagesCache = CreatedGameMessagesCache.getInstance();
        Message message = createdGameMessagesCache.getMessage(gameId);
        Message updatedMessage = message.editMessage("```" + newGameTable + "```").complete(false);
        createdGameMessagesCache.putMessage(gameId.toString(), updatedMessage);
    }
}
