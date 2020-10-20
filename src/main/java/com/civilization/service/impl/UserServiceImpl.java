package com.civilization.service.impl;

import com.civilization.dto.GameResultDTO;
import com.civilization.dto.ScrappedActiveGameDTO;
import com.civilization.mapper.GameResultsMapper;
import com.civilization.mapper.ScrappedActiveGameMapper;
import com.civilization.model.*;
import com.civilization.repository.ActiveGameRepository;
import com.civilization.repository.GameResultRepository;
import com.civilization.repository.UserActiveGameRepository;
import com.civilization.repository.UserRepository;
import com.civilization.service.NationRollService;
import com.civilization.service.UserService;
import io.vavr.API;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final long DEFAULT_RAITING = 1000L;
    public static final int BONUS_RATING_FOR_ALIVE = 10;
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
    private NationRollService nationRollService;

    @Autowired
    private GameResultsMapper gameResultsMapper;
    @Autowired
    private ScrappedActiveGameMapper scrappedActiveGameMapper;

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
        List<Nation> rolls = nationRollService.ffaSixRoll();
        addNewActiveGameForEachUser(users, rolls, hostname);
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

    @Override
    @Transactional
    public List<ScrappedActiveGameDTO> scrapOldGames() {
        List<ActiveGame> games = activeGameRepository.getGamesOlderThenWeek();
        scrapAllGames(games);

        return games.stream()
                .map(scrappedActiveGameMapper::map)
                .collect(Collectors.toList());
    }

    private void scrapAllGames(List<ActiveGame> games) {
        games.forEach(this::scrapGame);
    }

    private void scrapGame(ActiveGame activeGame) {
        activeGame.setGameStatus(GameStatus.SCRAP);
        activeGameRepository.save(activeGame);
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
        if (isGameReported(activeGame)) {
            throw new Exception("this game was already reported");
        }

        if (!isGameStarted(activeGame) && !(isAdmin && isGameFrozen(activeGame))) {
            throw new Exception("this game was not started yet or was frozen");
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

    //TODO: move to the dto
    private boolean isGameFrozen(ActiveGame activeGame) {
        return GameStatus.FREEZE.equals(activeGame.getGameStatus());
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
        concreteGame.setGameStatus(GameStatus.REPORTED);
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

    private void addNewActiveGameForEachUser(Set<User> users, List<Nation> rolls, String hostname) {
        ActiveGame activeGame = activeGameRepository.save(new ActiveGame());
        int slot = 1;
        ArrayList<User> shuffledUsers = new ArrayList<>(users);
        Collections.shuffle(shuffledUsers);
        Iterator<Nation> rollsIterator = rolls.iterator();
        for (User user: shuffledUsers) {
            UserActiveGame userActiveGame = new UserActiveGame(user, activeGame);
            if (user.getUsername().equalsIgnoreCase(hostname)) {
                userActiveGame.setGameHost(Boolean.TRUE);
            }
            userActiveGame.setSlot(slot++);
            userActiveGame.setUserNationRoll(getRollsForPlayer(rollsIterator));
            user.getUserActiveGames().add(userActiveGame);
            activeGame.getUserActiveGames().add(userActiveGame);
            userActiveGameRepository.save(userActiveGame);
        }
    }

    private UserNationRoll getRollsForPlayer(Iterator<Nation> rollsIterator) {
        UserNationRoll userNationRoll = new UserNationRoll();
        userNationRoll.addNationRoll(rollsIterator.next());
        userNationRoll.addNationRoll(rollsIterator.next());
        userNationRoll.addNationRoll(rollsIterator.next());
        return userNationRoll;
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

    //TODO: move all this to DTO
    private boolean isGameStarted(ActiveGame activeGame) {
        return GameStatus.START.equals(activeGame.getGameStatus());
    }

    //TODO: move all this to DTO
    private boolean isGameReported(ActiveGame activeGame) {
        return GameStatus.REPORTED.equals(activeGame.getGameStatus());
    }
}
