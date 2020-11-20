package com.civilization.bot.event;

import com.civilization.bot.event.operation.EventOperation;
import com.civilization.bot.event.rule.MessageListenedAppliedRule;
import com.civilization.bot.event.validator.MessageValidator;
import com.civilization.configuration.custom.DiscordMessageListenerQualifier;
import com.civilization.configuration.custom.annotation.RatingGamesChannelEventAnnotation;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@DiscordMessageListenerQualifier
@RatingGamesChannelEventAnnotation
public class CreateFFAReportMessageListener extends BaseMessageListener {

    private EventOperation eventOperation;

    @Autowired
    protected CreateFFAReportMessageListener(
            MessageValidator messageValidator,
            @Qualifier("createFFAReportOperation") EventOperation eventOperation,
            @Qualifier("createFFAReportMessageRule") MessageListenedAppliedRule messageRule) {
        super(messageValidator, eventOperation, messageRule);

        this.eventOperation = eventOperation;
    }

    @Override
    protected void sendMessage(MessageReceivedEvent event) throws Exception {
        String table = eventOperation.execute(event);
        event.getChannel().sendMessage("```" + table + "```").complete(true);
    }
}
