package com.civilization.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.civilization.model.ActiveGame;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;
import com.civilization.repository.ActiveGameRepository;
import com.civilization.service.ActiveGameService;

@Service
public class ActiveGameServiceImpl implements ActiveGameService {
    @Autowired
    private ActiveGameRepository activeGameRepository;

    @Override
    @Transactional
    public Optional<ActiveGame> setUserConfirmedGame(Long gameId, String username) {
        Optional<ActiveGame> activeGame = activeGameRepository.findById(gameId);
        if (!activeGame.isPresent() || !isUserInGameList(activeGame.get(), username)) {
            return Optional.empty();
        }
        ActiveGame game = activeGame.get();
        Optional<User> user = getUserToConfirmParticipation(username, activeGame);
        if (user.isPresent()) {
            confirmParticipationInActiveGame(user.get(), gameId);
        }
        if (shouldBeActiveGameStarted(activeGame)) {
            game.setStarted(Boolean.TRUE);
            game.setStartDate(LocalDateTime.now());
        }
        activeGameRepository.save(game);
        return Optional.of(game);
    }

    private void confirmParticipationInActiveGame(User user, Long gameId) {
        user.getUserActiveGames().stream()
                .filter(uag -> uag.getActiveGame().getId() == gameId)
                .forEach(uag -> uag.setGameConfirmed(Boolean.TRUE));
    }

    @Override
    @Transactional
    public Optional<ActiveGame> setUserDeclinedGame(Long gameId, String username) {
        Optional<ActiveGame> activeGame = activeGameRepository.findById(gameId);
        if (!activeGame.isPresent() || activeGame.get().isStarted() || !isUserInGameList(activeGame.get(), username)) {
            return Optional.empty();
        }
        ActiveGame game = activeGame.get();
//        game.getUserActiveGames().forEach(user -> user.getActiveGames().remove(game));
        activeGameRepository.delete(game);
        return activeGame;
    }

    private Optional<User> getUserToConfirmParticipation(String username, Optional<ActiveGame> activeGame) {
        for (UserActiveGame uag : activeGame.get().getUserActiveGames()) {
            if (uag.getUser().getUsername().equalsIgnoreCase(username)) {
                return Optional.ofNullable(uag.getUser());
            }
        }
        return Optional.empty();
    }

    private boolean shouldBeActiveGameStarted(Optional<ActiveGame> activeGame) {
        return activeGame.get().getUserActiveGames().stream().allMatch(UserActiveGame::isGameConfirmed);
    }

    private boolean isUserInGameList(ActiveGame activeGame, String username) {
        return activeGame.getUserActiveGames().stream().anyMatch(uag -> uag.getUser().getUsername().equalsIgnoreCase(username));
    }
}
