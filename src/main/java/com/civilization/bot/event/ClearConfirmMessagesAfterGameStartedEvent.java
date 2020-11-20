package com.civilization.bot.event;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClearConfirmMessagesAfterGameStartedEvent {

    public static final int MAX_LIMIT_OF_RETRIEVED_MESSAGES = 100;

    public void execute(Long gameId, MessageReceivedEvent event) {
        MessageHistory messageHistory = new MessageHistory(event.getChannel());
        List<Message> messages = messageHistory.retrievePast(MAX_LIMIT_OF_RETRIEVED_MESSAGES).complete();
        messages.stream()
                .filter(message -> message.getContentDisplay().equalsIgnoreCase(gameId + "+"))
                .forEach(message -> event.getChannel().deleteMessageById(message.getId()).complete());
    }
}
