package com.civilization.cache;

import com.civilization.cache.event.GameMessageEvent;
import com.civilization.cache.event.GameMessageEventManager;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;
import java.util.Map;

public class CreatedGameMessagesCache {

    private final GameMessageEventManager gameMessageEventManager = new GameMessageEventManager();

    private Map<String, Message> cache = new HashMap<>();

    private Map<String, String> cacheLanguage = new HashMap<>();

    private CreatedGameMessagesCache() {
    }

    public void putLanguage(String gameId, String language) {
        cacheLanguage.put(gameId, language);
    }

    public String getLanguage(String gameId) {
        return cacheLanguage.get(gameId);
    }

    public void putMessage(String gameId, Message message) {
        cache.put(gameId, message);
        gameMessageEventManager.notifyListeners(GameMessageEvent.NEW_MESSAGE_ADDED, new MessageGameIdPair(message, gameId));
    }

    public Message getMessage(String gameId) {
        return cache.get(gameId);
    }

    public Message getMessage(Long gameId) {
        return cache.get(gameId.toString());
    }

    public void removeMessage(String gameId) {
        Message removedMessage = cache.remove(gameId);
        gameMessageEventManager.notifyListeners(GameMessageEvent.MESSAGE_DELETED, new MessageGameIdPair(removedMessage, gameId));
    }

    private static class CreatedGameMessagesCacheSeed {
        private static final CreatedGameMessagesCache SEED = new CreatedGameMessagesCache();
    }

    public static CreatedGameMessagesCache getInstance(){
        return CreatedGameMessagesCacheSeed.SEED;
    }
}
