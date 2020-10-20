package com.civilization.mapper;

import com.civilization.dto.LobbyDto;
import com.civilization.model.Lobby;
import org.springframework.stereotype.Component;

@Component
public class LobbyMapper {
    public LobbyDto map(Lobby lobby) {
        return new LobbyDto(lobby.getGameId(), lobby.getLobbyMessageId());
    }
}
