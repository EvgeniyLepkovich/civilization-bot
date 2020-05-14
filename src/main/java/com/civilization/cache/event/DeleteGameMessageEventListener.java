package com.civilization.cache.event;

import com.civilization.cache.MessageGameIdPair;
import com.civilization.configuration.custom.GameMessageCacheListenersQualifier;
import org.springframework.stereotype.Component;

public class DeleteGameMessageEventListener implements EventListener {

    private final GameMessageEvent activator = GameMessageEvent.MESSAGE_DELETED;

    @Override
    public void execute(GameMessageEvent event, MessageGameIdPair messageGameIdPair) {
        if (!activator.equals(event)) {
            return;
        }

        messageGameIdPair.getFirst().delete().queue();
    }

    @Override
    public GameMessageEvent getActivator() {
        return activator;
    }
}
