package com.civilization.bot.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.civilization.configuration.custom.annotation.RatingGamesChannelEventAnnotation;
import com.civilization.exception.CodedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;
import com.civilization.cache.CreatedGameMessagesCache;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;
import com.civilization.util.GameIdParser;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Component
@DiscordMessageListenerQualifier
@RatingGamesChannelEventAnnotation
public class CreateFFAGameMessageListener extends BaseMessageListener {

    private EventOperation eventOperation;
    private CreatedGameMessagesCache createdGameMessagesCache;
    private GameIdParser gameIdParser;

    @Autowired
    protected CreateFFAGameMessageListener(
            MessageValidator messageValidator,
            @Qualifier("createFFAGameOperation") EventOperation eventOperation,
            @Qualifier("createFFAGameMessageRule") MessageListenedAppliedRule messageRule,
            CreatedGameMessagesCache createdGameMessagesCache,
            GameIdParser gameIdParser) {
        super(messageValidator, eventOperation, messageRule);

        this.gameIdParser = gameIdParser;
        this.eventOperation = eventOperation;
        this.createdGameMessagesCache = createdGameMessagesCache;
    }

    @Override
    protected void sendMessage(MessageReceivedEvent event) throws Exception {
        try {
            MessageEmbed messageEmbed = eventOperation.executeForMessageEmbed(event);
            Message message = event.getChannel().sendMessage(messageEmbed).complete(true);
            createdGameMessagesCache.putMessage(gameIdParser.getGameId(messageEmbed.getTitle()), message);
        } catch (CodedException e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
    }
}
