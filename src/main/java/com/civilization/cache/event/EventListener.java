package com.civilization.cache.event;

import com.civilization.cache.MessageGameIdPair;

public abstract class EventListener {

    public void execute(GameMessageEvent event, MessageGameIdPair messageGameIdPair) {
        if (!getActivator().equals(event)) {
            return;
        }
        executeEvent(event, messageGameIdPair);
    }

    protected abstract void executeEvent(GameMessageEvent event, MessageGameIdPair messageGameIdPair);

    protected abstract GameMessageEvent getActivator();
}
