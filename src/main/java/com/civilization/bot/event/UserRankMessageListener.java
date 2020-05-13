package com.civilization.bot.event;

import com.civilization.configuration.custom.annotation.RatingGamesChannelEventAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Component
@DiscordMessageListenerQualifier
@RatingGamesChannelEventAnnotation
public class UserRankMessageListener extends BaseMessageListener {

    @Autowired
    protected UserRankMessageListener(
            MessageValidator messageValidator,
            @Qualifier("userRankOperation") EventOperation eventOperation,
            @Qualifier("UserRankMessageRule") MessageListenedAppliedRule messageRule) {
        super(messageValidator, eventOperation, messageRule);
    }
}
