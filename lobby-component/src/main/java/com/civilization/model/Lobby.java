package com.civilization.model;

import javax.persistence.*;

@Entity
public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lobby_generator")
    @SequenceGenerator(name = "lobby_generator", sequenceName = "lobby_sequence", initialValue = 2500)
    @Column(name = "lobby_id")
    private long id;

    private long gameId;

    @Enumerated(EnumType.STRING)
    private LobbyStatus lobbyStatus;

    private String lobbyMessageId;

    public Lobby() {
    }

    public Lobby(Long gameId, String lobbyMessageId, LobbyStatus lobbyStatus) {
        this.gameId = gameId;
        this.lobbyMessageId = lobbyMessageId;
        this.lobbyStatus = lobbyStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public LobbyStatus getLobbyStatus() {
        return lobbyStatus;
    }

    public void setLobbyStatus(LobbyStatus lobbyStatus) {
        this.lobbyStatus = lobbyStatus;
    }

    public String getLobbyMessageId() {
        return lobbyMessageId;
    }

    public void setLobbyMessageId(String lobbyMessageId) {
        this.lobbyMessageId = lobbyMessageId;
    }
}
