package com.civilization.cache.event;

import com.civilization.cache.MessageGameIdPair;

public class DeleteGameMessageEventListener extends EventListener {

    @Override
    protected void executeEvent(GameMessageEvent event, MessageGameIdPair messageGameIdPair) {
        messageGameIdPair.getFirst().delete().queue();
    }

    @Override
    protected GameMessageEvent getActivator() {
        return GameMessageEvent.MESSAGE_DELETED;
    }

}
