package com.civilization.service.impl;

import com.civilization.dto.LobbyDto;
import com.civilization.mapper.LobbyMapper;
import com.civilization.model.Lobby;
import com.civilization.model.LobbyStatus;
import com.civilization.repository.LobbyRepository;
import com.civilization.service.LobbyService;
import com.civilization.timer.CloseGameTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LobbyServiceImpl implements LobbyService {

    @Autowired
    private LobbyRepository lobbyRepository;
    @Autowired
    private CloseGameTimer closeGameTimer;
    @Autowired
    private LobbyMapper lobbyMapper;

    @Override
    public void createLobby(LobbyDto lobbyDto) {
        Long gameId = lobbyDto.getGameId();
        lobbyRepository.save(new Lobby(gameId, lobbyDto.getLobbyMessageId(), LobbyStatus.CREATED));
        closeGameTimer.runCloseGameTimer(lobbyDto);
    }

    @Override
    public void startLobbyGame(LobbyDto lobbyDto) {
        updateLobbyStatus(lobbyDto.getGameId(), LobbyStatus.STARTED);
    }

    @Override
    public void closeLobbyGame(LobbyDto lobbyDto) {
        updateLobbyStatus(lobbyDto.getGameId(), LobbyStatus.CLOSED);
    }

    private void updateLobbyStatus(Long gameId, LobbyStatus lobbyStatus) {
        Lobby lobby = lobbyRepository.findByGameId(gameId);
        lobby.setLobbyStatus(lobbyStatus);
        lobbyRepository.save(lobby);
    }

    @Override
    public boolean isLobbyGameStarted(LobbyDto lobbyDto) {
        return lobbyRepository.isGameStarted(lobbyDto.getGameId());
    }

    @Override
    public boolean isLobbyClosed(LobbyDto lobbyDto) {
        return lobbyRepository.isLobbyClosed(lobbyDto.getGameId());
    }

    @Override
    public boolean isLobbyCreated(LobbyDto lobbyDto) {
        return lobbyRepository.isLobbyCreated(lobbyDto.getGameId());
    }

    @Override
    public LobbyDto getLobbyByGameId(Long gameId) {
        return lobbyMapper.map(lobbyRepository.findByGameId(gameId));
    }
}
