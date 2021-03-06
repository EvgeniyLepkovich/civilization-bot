package com.civilization.service.impl;

import com.civilization.dto.LobbyDto;
import com.civilization.exception.GameNotFoundException;
import com.civilization.exception.GameNotStartedException;
import com.civilization.model.ActiveGame;
import com.civilization.model.GameStatus;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;
import com.civilization.repository.ActiveGameRepository;
import com.civilization.service.ActiveGameService;
import com.civilization.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ActiveGameServiceImpl implements ActiveGameService {
    @Autowired
    private ActiveGameRepository activeGameRepository;
    @Autowired
    private LobbyService lobbyService;

    @Override
    @Transactional
    public Optional<ActiveGame> setUserConfirmedGame(Long gameId, String username) {
        Optional<ActiveGame> activeGame = activeGameRepository.findById(gameId);
        if (!activeGame.isPresent()) {
            throw new RuntimeException("game doesn't exist");
        }
        ActiveGame game = activeGame.get();

        validateUser(game, username);

        Optional<User> user = getUserToConfirmParticipation(username, activeGame);
        if (user.isPresent()) {
            confirmParticipationInActiveGame(user.get(), gameId);
        }

        if (shouldBeActiveGameStarted(activeGame)) {
            game.setGameStatus(GameStatus.START);
            game.setStartDate(LocalDateTime.now());
        }
        activeGameRepository.save(game);
        return Optional.of(game);
    }

    private void validateUser(ActiveGame activeGame, String username) {
        boolean isValid =
                lobbyService.isLobbyCreated(new LobbyDto(activeGame.getId())) &&
                isUserInGameList(activeGame, username);

        if (!isValid) {
            throw new RuntimeException(String.format("Cannot confirm game #%s for user %s", activeGame.getId(), username));
        }
    }

    private void confirmParticipationInActiveGame(User user, Long gameId) {
        user.getUserActiveGames().stream()
                .filter(uag -> uag.getActiveGame().getId() == gameId)
                .forEach(uag -> uag.setGameConfirmed(Boolean.TRUE));
    }

    @Override
    public Optional<ActiveGame> setUserDeclinedGame(Long gameId, String username) {
        Optional<ActiveGame> activeGame = activeGameRepository.findById(gameId);
        if (!activeGame.isPresent()) {
            throw new RuntimeException("game doesn't exist");
        }
        ActiveGame game = activeGame.get();

        validateUser(game, username);
        activeGameRepository.delete(game);
        return activeGame;
    }

    @Override
    public void freezeGame(Long gameId) {
        //TODO: move validation to separate class
        Optional<ActiveGame> activeGame = activeGameRepository.findById(gameId);
        if (!activeGame.isPresent()) {
            throw new GameNotFoundException();
        }

        ActiveGame ag = activeGame.get();
        if (!GameStatus.START.equals(ag.getGameStatus())) {
            throw new GameNotStartedException();
        }

        ag.setGameStatus(GameStatus.FREEZE);
        activeGameRepository.save(ag);
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

    //TODO: move all this to DTO
    private boolean isGameStarted(ActiveGame activeGame) {
        return GameStatus.START.equals(activeGame.getGameStatus());
    }
}
