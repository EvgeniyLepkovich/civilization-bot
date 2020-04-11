package com.civilization.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.civilization.dto.GameResultDTO;
import com.civilization.mapper.decorator.GameResultsMapper;
import com.civilization.model.ActiveGame;
import com.civilization.model.GameResult;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;
import com.civilization.model.UserGameResult;
import com.civilization.model.UserRank;
import com.civilization.repository.ActiveGameRepository;
import com.civilization.repository.GameResultRepository;
import com.civilization.repository.UserActiveGameRepository;
import com.civilization.repository.UserRepository;
import com.civilization.service.UserService;

import io.vavr.API;

@Service
public class UserServiceImpl implements UserService {
    private static final long DEFAULT_RAITING = 1000L;
    public static final int BONUS_RATING_FOR_ALIVE = 15;
    public static final int KOEF_FOR_LEAVERS = 3;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActiveGameRepository activeGameRepository;
    @Autowired
    private UserActiveGameRepository userActiveGameRepository;
    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private GameResultsMapper gameResultsMapper;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<User> createFFAGameForUsers(List<String> usernames, String hostname) {
        Set<User> users = getOrCreateUsers(usernames);
        addNewActiveGameForEachUser(users, hostname);
        return (List<User>) userRepository.saveAll(users);
    }

    @Override
    @Transactional
    public List<GameResultDTO> createFFAReport(List<GameResultDTO> gameResults, String eventOwner) throws Exception {
        return this.createFFAReport(gameResults, eventOwner, false);
    }

    @Override
    @Transactional
    public List<GameResultDTO> createFFAReport(List<GameResultDTO> gameResults, String eventOwner, boolean isAdmin) throws Exception {
        validateGameResults(gameResults, eventOwner, isAdmin);
        addDestroyedUsersToGameResults(gameResults);
        calculateRating(gameResults);
        List<User> users = toUsers(gameResults);
        List<User> savedUsers = saveResults(users);
        return gameResultsMapper.map(savedUsers, gameResults.get(0).getGameId());
    }

    private void addDestroyedUsersToGameResults(List<GameResultDTO> gameResults) throws Exception {
        ActiveGame activeGame = getActiveGame(gameResults.get(0).getGameId());
        List<User> destroyedUsers = getListOfDestroyedUsers(activeGame, gameResults);
        gameResults.addAll(toGameResult(activeGame, destroyedUsers));
    }

    private List<GameResultDTO> toGameResult(ActiveGame activeGame, List<User> destroyedUsers) {
        return destroyedUsers.stream()
                .map(user -> new GameResultDTO(activeGame.getId(), user.getUsername(), UserGameResult.DESTROYED))
                .collect(Collectors.toList());
    }

    private List<User> getListOfDestroyedUsers(ActiveGame activeGame, List<GameResultDTO> gameResults) {
        return activeGame.getUserActiveGames().stream()
                .map(UserActiveGame::getUser)
                .filter(user -> !isUsernameInGameResultList(user.getUsername(), gameResults))
                .collect(Collectors.toList());

    }

    private boolean isUsernameInGameResultList(String username, List<GameResultDTO> gameResults) {
        return gameResults.stream()
                .anyMatch(gameResult -> gameResult.getUsername().equalsIgnoreCase(username));
    }

    private void validateGameResults(List<GameResultDTO> gameResults, String eventOwner, boolean isAdmin) throws Exception {
        if (CollectionUtils.isEmpty(gameResults)) {
            throw new Exception("no game results");
        }

        ActiveGame activeGame = getActiveGame(gameResults.get(0).getGameId());
        if (activeGame.isReported()) {
            throw new Exception("this game was already reported");
        }

        if (!isAllPlayersParticipantOnGame(activeGame, gameResults)) {
            throw new Exception("not all users are participants of game");
        }
        if (isAdmin) {
            return;
        }
        if (!isHostedByGameOwner(activeGame, eventOwner)) {
            throw new Exception("reported not by game owner");
        }
    }

    private boolean isHostedByGameOwner(ActiveGame activeGame, String eventOwner) throws Exception {
        return getGameHost(activeGame).equalsIgnoreCase(eventOwner);
    }

    private String getGameHost(ActiveGame activeGame) throws Exception {
        return activeGame.getUserActiveGames().stream()
                .filter(UserActiveGame::isGameHost)
                .map(UserActiveGame::getUser)
                .map(User::getUsername)
                .findFirst()
                .orElseThrow(() -> new Exception("can't find game host"));
    }

    private boolean isAllPlayersParticipantOnGame(ActiveGame activeGame, List<GameResultDTO> gameResults) {
        return gameResults.stream().allMatch(gameResult -> isUserInGame(gameResult.getUsername(), activeGame));
    }

    private boolean isUserInGame(String username, ActiveGame activeGame) {
        return activeGame.getUserActiveGames().stream()
                .anyMatch(uag -> uag.getUser().getUsername().equalsIgnoreCase(username));
    }

    private ActiveGame getActiveGame(Long gameId) throws Exception {
        return activeGameRepository.findById(gameId).orElseThrow(() -> new Exception("Can't find the game"));
    }

    private List<User> saveResults(List<User> users) {
        return (List<User>) userRepository.saveAll(users);
    }

    private List<User> toUsers(List<GameResultDTO> gameResults) {
        return gameResults.stream()
                .map(API.unchecked(this::toUser))
                .collect(Collectors.toList());
    }

    private User toUser(GameResultDTO gameResultDTO) throws Exception {
        GameResult gameResult = toGameResult(gameResultDTO);

        User user = userRepository.findByUsername(gameResultDTO.getUsername());
        ActiveGame concreteGame = findConcreteGame(user, gameResultDTO.getGameId());
        concreteGame.setReported(Boolean.TRUE);
        concreteGame.getGameResult().add(gameResult);
        gameResult.setActiveGame(concreteGame);
        gameResult.setUser(user);
        user.getGameResults().add(gameResult);
        user.setRating(gameResultDTO.getNewRating());
        gameResultRepository.save(gameResult);
        return user;
    }

    private GameResult toGameResult(GameResultDTO gameResultDTO) {
        return new GameResult(gameResultDTO.getGameResult(), gameResultDTO.getOldRating(), gameResultDTO.getNewRating());
    }

    private ActiveGame findConcreteGame(User user, Long gameId) throws Exception {
        return user.getUserActiveGames().stream()
                .map(UserActiveGame::getActiveGame)
                .filter(game -> game.getId() == gameId)
                .findFirst()
                .orElseThrow(() ->new Exception("Game with id: " + gameId + ", was not detected"));
    }

    private void calculateRating(List<GameResultDTO> gameResults) throws Exception {
        GameResultDTO winner = findWinnerResult(gameResults);
        gameResults.forEach(gameResult -> calculateRating(gameResult, winner));

        //rating for winner depends on all other calculations
        calculateRatingForWinner(gameResults, winner);
    }

    private void calculateRatingForWinner(List<GameResultDTO> gameResults, GameResultDTO winner) throws Exception {
        Long totalChangeRating = calculateTotalChangeRatingWithoutBonuses(gameResults, winner);

        gameResults.stream()
                .filter(gameResult -> UserGameResult.WINNER.equals(gameResult.getGameResult()))
                .forEach(gameResult -> gameResult.setNewRating(gameResult.getOldRating() + totalChangeRating));
    }

    private GameResultDTO findWinnerResult(List<GameResultDTO> gameResults) throws Exception {
        return gameResults.stream()
                .filter(gameResult -> UserGameResult.WINNER.equals(gameResult.getGameResult()))
                .findFirst()
                .orElseThrow(() -> new Exception("can't find winner"));
    }

    private Long calculateTotalChangeRatingWithoutBonuses(List<GameResultDTO> gameResults, GameResultDTO winner) {
        return gameResults.stream()
                .filter(gameResult -> !UserGameResult.WINNER.equals(gameResult.getGameResult()))
                //for winner old rating and new rating are similar here
                .map(gameResult -> Math.abs(Math.round(gameResult.getOldRating().doubleValue() / winner.getOldRating().doubleValue() * 10)))
                .reduce(0L, Long::sum);
    }

    private void calculateRating(GameResultDTO gameResult, GameResultDTO winner) {
        Long currentRating = userRepository.findCurrentRating(gameResult.getUsername());
        Long newRating = calculateRating(currentRating, gameResult.getGameResult(), winner.getOldRating());
        gameResult.setOldRating(currentRating);
        gameResult.setNewRating(newRating);
    }

    private Long calculateRating(Long currentRating, UserGameResult gameResult, Long winnerRating) {
        if (UserGameResult.WINNER.equals(gameResult)) {
            return calculateRatingForWinner(currentRating);
        }
        if (UserGameResult.ALIVE.equals(gameResult)) {
            return calculateRatingForAlive(currentRating, winnerRating);
        }
        if (UserGameResult.DESTROYED.equals(gameResult)) {
            return calculateRatingForDestroyed(currentRating, winnerRating);
        }

        return calculateRatingForLeaver(currentRating, winnerRating);
    }

    private Long calculateRatingForDestroyed(Long currentRating, Long winnerRating) {
        return currentRating - Math.round(currentRating.doubleValue() / winnerRating.doubleValue() * 10);
    }

    private Long calculateRatingForLeaver(Long currentRating, Long winnerRating) {
        return currentRating - Math.round((currentRating.doubleValue() / winnerRating.doubleValue()) * 10 * KOEF_FOR_LEAVERS);
    }

    private Long calculateRatingForAlive(Long currentRating, Long winnerRating) {
        return currentRating + Math.round((-(currentRating.doubleValue() / winnerRating.doubleValue()) * 10 + BONUS_RATING_FOR_ALIVE));
    }

    private Long calculateRatingForWinner(Long currentRating) {
        return currentRating;
    }

    @Override
    public UserRank findUserRank(String username) {
        return userRepository.findUserRank(username);
    }

    private void addNewActiveGameForEachUser(Set<User> users, String hostname) {
        ActiveGame activeGame = activeGameRepository.save(new ActiveGame());
        for (User user: users) {
            UserActiveGame userActiveGame = new UserActiveGame(user, activeGame);
            if (user.getUsername().equalsIgnoreCase(hostname)) {
                userActiveGame.setGameHost(Boolean.TRUE);
            }
            user.getUserActiveGames().add(userActiveGame);
            activeGame.getUserActiveGames().add(userActiveGame);
            userActiveGameRepository.save(userActiveGame);
        }
    }

    private Set<User> getOrCreateUsers(List<String> usernames) {
        return usernames.stream()
                .map(this::getOrCreateUser)
                .collect(Collectors.toSet());
    }

    private User getOrCreateUser(String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? userRepository.save(new User(username, DEFAULT_RAITING)) : user;
    }
}
