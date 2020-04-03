package com.civilization.bot.event;

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
        MessageEmbed messageEmbed = eventOperation.executeForMessageEmbed(event);
        event.getChannel().sendMessage(messageEmbed).complete(true);
    }
}
