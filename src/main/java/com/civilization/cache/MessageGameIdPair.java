package com.civilization.cache;

import net.dv8tion.jda.core.entities.Message;

public class MessageGameIdPair {
    private Message first;
    private String second;

    public MessageGameIdPair(Message first, String second) {
        this.first = first;
        this.second = second;
    }

    public Message getFirst() {
        return first;
    }

    public void setFirst(Message first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}
