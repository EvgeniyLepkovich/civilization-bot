package com.civilization.dto;

public class LobbyDto {

    private Long gameId;
    private String lobbyMessageId;

    public LobbyDto(Long gameId) {
        this(gameId, "");
    }

    public LobbyDto(Long gameId, String lobbyMessageId) {
        this.gameId = gameId;
        this.lobbyMessageId = lobbyMessageId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getLobbyMessageId() {
        return lobbyMessageId;
    }

    public void setLobbyMessageId(String lobbyMessageId) {
        this.lobbyMessageId = lobbyMessageId;
    }
}
