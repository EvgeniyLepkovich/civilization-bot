package com.civilization.mapper;

import com.civilization.dto.ScrappedActiveGameDTO;
import com.civilization.model.ActiveGame;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScrappedActiveGameMapper {

    public ScrappedActiveGameDTO map(ActiveGame activeGame) {
        ScrappedActiveGameDTO dto = new ScrappedActiveGameDTO();
        dto.setGameId(activeGame.getId());
        dto.setUsernames(getUsernames(activeGame));
        return dto;
    }

    private List<String> getUsernames(ActiveGame activeGame) {
        return activeGame.getUserActiveGames().stream()
                .map(uag -> uag.getUser().getUsername())
                .collect(Collectors.toList());
    }
}
