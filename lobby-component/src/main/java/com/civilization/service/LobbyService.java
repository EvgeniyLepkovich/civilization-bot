package com.civilization.service;

import com.civilization.dto.LobbyDto;

public interface LobbyService {
    void createLobby(LobbyDto lobbyDto);
    void startLobbyGame(LobbyDto lobbyDto);
    void closeLobbyGame(LobbyDto lobbyDto);
    boolean isLobbyGameStarted(LobbyDto lobbyDto);
    boolean isLobbyClosed(LobbyDto lobbyDto);
    boolean isLobbyCreated(LobbyDto lobbyDto);
    LobbyDto getLobbyByGameId(Long gameId);
}
