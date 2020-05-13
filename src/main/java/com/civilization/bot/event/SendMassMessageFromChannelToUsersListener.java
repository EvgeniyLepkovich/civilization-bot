package com.civilization.bot.event;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@DiscordMessageListenerQualifier
public class SendMassMessageFromChannelToUsersListener extends BaseMessageListener {

    @Autowired
    protected SendMassMessageFromChannelToUsersListener(MessageValidator messageValidator,
                                                        @Qualifier("sendMassMessageFromChannelToUsersOperation") EventOperation eventOperation,
                                                        @Qualifier("sendMassMessageFromChannelToUsersRule") MessageListenedAppliedRule messageRule) {
        super(messageValidator, eventOperation, messageRule);
    }
}
