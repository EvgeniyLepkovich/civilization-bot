package com.civilization.service;

import com.civilization.model.ActiveGame;

import java.util.Optional;

public interface ActiveGameService {
    Optional<ActiveGame> setUserConfirmedGame(Long gameId, String username);
    Optional<ActiveGame> setUserDeclinedGame(Long gameId, String username);
}
