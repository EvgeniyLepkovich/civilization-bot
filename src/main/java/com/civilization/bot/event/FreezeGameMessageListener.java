package com.civilization.bot.event;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;
import com.civilization.configuration.custom.annotation.RatingGamesChannelEventAnnotation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@DiscordMessageListenerQualifier
@RatingGamesChannelEventAnnotation
public class FreezeGameMessageListener extends BaseMessageListener {

    protected FreezeGameMessageListener(
            MessageValidator messageValidator,
            @Qualifier("FreezeGameOperation") EventOperation eventOperation,
            @Qualifier("FreezeGameMessageRule") MessageListenedAppliedRule messageRule) {
        super(messageValidator, eventOperation, messageRule);
    }
}
