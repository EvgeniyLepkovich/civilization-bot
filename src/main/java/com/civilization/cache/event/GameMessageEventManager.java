package com.civilization.cache.event;

import com.civilization.cache.MessageGameIdPair;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class GameMessageEventManager {

    private static final Map<GameMessageEvent, List<EventListener>> listeners = new HashMap<>();

    public GameMessageEventManager() {
        subscribe(GameMessageEvent.MESSAGE_DELETED, new NotifyDiscordAboutDeletedMessageEventListener());
        subscribe(GameMessageEvent.NEW_MESSAGE_ADDED, new GameTimerEventListener());
        subscribe(GameMessageEvent.MESSAGE_DELETED, new DeleteGameMessageEventListener());
    }

    public void subscribe(GameMessageEvent event, EventListener listener) {
        List<EventListener> eventListeners = listeners.get(event);
        if (CollectionUtils.isEmpty(eventListeners)) {
            List<EventListener> newListeners = new ArrayList<>();
            newListeners.add(listener);
            listeners.put(event, newListeners);
        } else {
            eventListeners.add(listener);
        }
    }

    public void unsubscribe(GameMessageEvent event, EventListener listener) {
        Optional.ofNullable(listeners.get(event)).ifPresent(group -> group.remove(listener));
    }

    public void notifyListeners(GameMessageEvent event, MessageGameIdPair messageGameIdPair) {
        Optional.ofNullable(listeners.get(event)).ifPresent(l -> notifyAllListeners(event, l, messageGameIdPair));
    }

    private void notifyAllListeners(GameMessageEvent event, List<EventListener> eventListeners,
                                    MessageGameIdPair messageGameIdPair) {
        eventListeners.forEach(listener -> listener.execute(event, messageGameIdPair));
    }
}
