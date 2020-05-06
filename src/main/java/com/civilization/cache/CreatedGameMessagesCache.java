package com.civilization.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import net.dv8tion.jda.core.entities.Message;

@Component
public class CreatedGameMessagesCache {

    private Map<String, Message> cache = new HashMap<>();

    private Map<String, String> cacheLanguage = new HashMap<>();

    public void putLanguage(String gameId, String language) {
        cacheLanguage.put(gameId, language);
    }

    public String getLanguage(Long gameId) {
        return cacheLanguage.get(gameId);
    }

    public void putMessage(String gameId, Message message) {
        cache.put(gameId, message);
    }

    public Message getMessage(String gameId) {
        return cache.get(gameId);
    }

    public Message getMessage(Long gameId) {
        return cache.get(gameId.toString());
    }

    public void removeMessage(String gameId) {
        cache.remove(gameId);
    }

}
