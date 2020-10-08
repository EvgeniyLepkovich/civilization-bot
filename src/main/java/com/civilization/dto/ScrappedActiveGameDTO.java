package com.civilization.dto;

import java.util.List;
import java.util.stream.Collectors;

public class ScrappedActiveGameDTO {
    private Long gameId;
    private List<String> usernames;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public String getUsernamesAsString() {
        return String.join(", ", usernames);
    }

    @Override
    public String toString() {
        return "ScrappedActiveGameDTO{" +
                "gameId=" + gameId +
                ", usernames=" + usernames +
                '}';
    }
}
