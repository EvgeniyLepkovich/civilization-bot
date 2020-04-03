package com.civilization.bot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;

@Component
@DiscordMessageListenerQualifier
public class ConfirmParticipationInFFAGameMessageListener extends BaseMessageListener {

    @Autowired
    protected ConfirmParticipationInFFAGameMessageListener(
            MessageValidator messageValidator,
            @Qualifier("confirmParticipationInFFAGameOperation") EventOperation eventOperation,
            @Qualifier("confirmParticipationInFFAGameMessageRule") MessageListenedAppliedRule messageRule) {
        super(messageValidator, eventOperation, messageRule);
    }
}
