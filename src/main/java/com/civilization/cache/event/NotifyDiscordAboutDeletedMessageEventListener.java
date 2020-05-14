package com.civilization.cache.event;

import com.civilization.cache.MessageGameIdPair;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class NotifyDiscordAboutDeletedMessageEventListener implements EventListener {

    private final GameMessageEvent activator = GameMessageEvent.MESSAGE_DELETED;

    @Override
    public void execute(GameMessageEvent event, MessageGameIdPair messageGameIdPair) {
        if (!event.equals(activator)) {
            return;
        }

        messageGameIdPair.getFirst().getChannel().sendMessage(buildMessageForDiscord(messageGameIdPair)).queue();
    }

    private Message buildMessageForDiscord(MessageGameIdPair messageGameIdPair) {
        return new MessageBuilder().append(getMessageForDiscord(messageGameIdPair)).build();
    }

    private String getMessageForDiscord(MessageGameIdPair messageGameIdPair) {
        return "Game №" + messageGameIdPair.getSecond() + " was canceled cuz of not enough players in time";
    }

    @Override
    public GameMessageEvent getActivator() {
        return activator;
    }
}
