package com.civilization.cache.event;

import com.civilization.cache.MessageGameIdPair;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class NotifyDiscordAboutDeletedMessageEventListener extends EventListener {


    @Override
    protected void executeEvent(GameMessageEvent event, MessageGameIdPair messageGameIdPair) {
        messageGameIdPair.getFirst().getChannel().sendMessage(buildMessageForDiscord(messageGameIdPair)).queue();
    }

    @Override
    protected GameMessageEvent getActivator() {
        return GameMessageEvent.MESSAGE_DELETED;
    }

    private Message buildMessageForDiscord(MessageGameIdPair messageGameIdPair) {
        return new MessageBuilder().append(getMessageForDiscord(messageGameIdPair)).build();
    }

    private String getMessageForDiscord(MessageGameIdPair messageGameIdPair) {
        return "Game №" + messageGameIdPair.getSecond() + " was canceled cuz of not enough players in time";
    }

}
