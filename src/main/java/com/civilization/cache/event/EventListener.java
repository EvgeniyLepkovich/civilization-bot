package com.civilization.cache.event;

import com.civilization.cache.MessageGameIdPair;

public interface EventListener {
    void execute(GameMessageEvent event, MessageGameIdPair messageGameIdPair);
    GameMessageEvent getActivator();
}
