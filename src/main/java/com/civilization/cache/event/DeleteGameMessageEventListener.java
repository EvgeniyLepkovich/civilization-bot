package com.civilization.cache.event;

import com.civilization.cache.MessageGameIdPair;

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
