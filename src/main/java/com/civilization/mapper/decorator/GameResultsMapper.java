package com.civilization.mapper.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.civilization.cache.CreatedGameMessagesCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.civilization.dto.GameResultDTO;
//import com.civilization.mapper.GameResultsMapper;
import com.civilization.model.ActiveGame;
import com.civilization.model.GameResult;
import com.civilization.model.User;
import com.civilization.model.UserActiveGame;
import com.civilization.model.UserGameResult;

import io.vavr.API;

@Component
public class GameResultsMapper {

    @Autowired
    private CreatedGameMessagesCache cacheLanguage;

    private static final String WINNER_TAG_EN = "winner: ";
    private static final String ALIVE_TAG_EN = "alive: ";
    private static final String LEAVER_TAG_EN = "leave: ";
    private static final String WINNER_TAG_RU = "победитель: ";
    private static final String ALIVE_TAG_RU = "выживший: ";
    private static final String LEAVER_TAG_RU = "ливер: ";

//    @Override
    public List<GameResultDTO> map(String message) {
        String gameId = Long.valueOf(message.split(" ")[1]).toString();
        boolean isEnglish = cacheLanguage.getLanguage(gameId).contains("isEnglish");
        return new GameResultsFromMessageMapperDecoratorBuilder(isEnglish)
                .withGameId(message)
                .withWinners(message)
                .withAlives(message)
                .withLeavers(message)
                .build();
    }

//    @Override
    public List<GameResultDTO> map(List<User> users, Long gameId) {
        return users.stream()
                .map(API.unchecked(user -> toGameResultDTO(user, gameId)))
                .collect(Collectors.toList());
    }

    private GameResultDTO toGameResultDTO(User user, Long gameId) throws Exception {
        return new GameResultsFromUsersMapperDecoratorBuilder()
                .withGameId(gameId)
                .withUsername(user)
                .withOldRating(user, gameId)
                .withNewRating(user, gameId)
                .withGameResultForUser(user, gameId)
                .build();
    }

    private static class GameResultsFromUsersMapperDecoratorBuilder {
        private GameResultDTO gameResultDTO = new GameResultDTO();

        private GameResultsFromUsersMapperDecoratorBuilder withGameId(Long gameId) {
            gameResultDTO.setGameId(gameId);
            return this;
        }

        private GameResultsFromUsersMapperDecoratorBuilder withUsername(User user) {
            gameResultDTO.setUsername(user.getUsername());
            return this;
        }

        private GameResultsFromUsersMapperDecoratorBuilder withOldRating(User user, Long gameId) throws Exception {
            ActiveGame activeGame = getActiveGame(user, gameId);
            gameResultDTO.setOldRating(findConcreteGameResult(user.getId(), activeGame.getId(), activeGame.getGameResult()).getOldRating());
            return this;
        }

        private GameResult findConcreteGameResult(Long userId, Long activeGameId, Set<GameResult> gameResults) throws Exception {
            return gameResults.stream()
                    .filter(gameResult -> gameResult.getUser().getId() == userId)
                    .filter(gameResult -> gameResult.getActiveGame().getId() == activeGameId)
                    .findFirst().orElseThrow(() -> new Exception("can't find game results"));
        }

        private GameResultsFromUsersMapperDecoratorBuilder withNewRating(User user, Long gameId) throws Exception {
            ActiveGame activeGame = getActiveGame(user, gameId);
            gameResultDTO.setNewRating(findConcreteGameResult(user.getId(), activeGame.getId(), activeGame.getGameResult()).getNewRating());
            return this;
        }

        private GameResultsFromUsersMapperDecoratorBuilder withGameResultForUser(User user, Long gameId) throws Exception {
            ActiveGame activeGame = getActiveGame(user, gameId);
            gameResultDTO.setGameResult(findConcreteGameResult(user.getId(), activeGame.getId(), activeGame.getGameResult()).getUserGameResult());
            return this;
        }

        private GameResultDTO build() {
            return gameResultDTO;
        }

        private ActiveGame getActiveGame(User user, Long gameId) throws Exception {
            return user.getUserActiveGames().stream()
                    .map(UserActiveGame::getActiveGame)
                    .filter(ag -> ag.getId() == gameId)
                    .findFirst()
                    .orElseThrow(() -> new Exception("can't find the game during mapping"));
        }
    }

    private static class GameResultsFromMessageMapperDecoratorBuilder {
        private static String WINNER_TAG;
        private static String ALIVE_TAG;
        private static String LEAVER_TAG;

        public GameResultsFromMessageMapperDecoratorBuilder(boolean isEnglish) {
            if (isEnglish) {
                WINNER_TAG = WINNER_TAG_EN;
                ALIVE_TAG = ALIVE_TAG_EN;
                LEAVER_TAG = LEAVER_TAG_EN;
            } else {
                WINNER_TAG = WINNER_TAG_RU;
                ALIVE_TAG = ALIVE_TAG_RU;
                LEAVER_TAG = LEAVER_TAG_RU;
            }
        }

        private final Function<String, Integer> endIndexForWinner = message ->
                message.contains(ALIVE_TAG) ? message.indexOf(ALIVE_TAG) - 1 : message.length();
        private final Function<String, String> winnerUsernamesString = message ->
                message.substring(message.indexOf(WINNER_TAG) + WINNER_TAG.length(), endIndexForWinner.apply(message));

        private final Function<String, Integer> endIndexForAlive = message ->
                message.contains(LEAVER_TAG) ? message.indexOf(LEAVER_TAG) - 1 : message.length();
        private final Function<String, String> aliveUsernamesString = message ->
                message.substring(message.indexOf(ALIVE_TAG) + ALIVE_TAG.length(), endIndexForAlive.apply(message));

        private final Function<String, String> leaverUsernamesString = message ->
                message.substring(message.indexOf(LEAVER_TAG) + LEAVER_TAG.length());

        private List<GameResultDTO> gameResults = new ArrayList<>();
        private Long gameId;

        private GameResultsFromMessageMapperDecoratorBuilder withGameId(String message) {
            this.gameId = Long.valueOf(message.split(" ")[1]);
            return this;
        }

        private GameResultsFromMessageMapperDecoratorBuilder withWinners(String message) {
            if (!message.contains(WINNER_TAG)) {
                return this;
            }

            gameResults.addAll(withGameResultForUsers(winnerUsernamesString.apply(message), UserGameResult.WINNER));
            return this;
        }

        private GameResultsFromMessageMapperDecoratorBuilder withAlives(String message) {
            if (!message.contains(ALIVE_TAG)) {
                return this;
            }

            gameResults.addAll(withGameResultForUsers(aliveUsernamesString.apply(message), UserGameResult.ALIVE));
            return this;
        }

        private GameResultsFromMessageMapperDecoratorBuilder withLeavers(String message) {
            if (!message.contains(LEAVER_TAG)) {
                return this;
            }

            gameResults.addAll(withGameResultForUsers(leaverUsernamesString.apply(message), UserGameResult.LEAVE));
            return this;
        }

        private List<GameResultDTO> withGameResultForUsers(String message, UserGameResult userGameResult) {
            return Stream.of(message.split("\\s"))
                    .map(word -> new GameResultDTO(gameId, word.replace("@", ""), userGameResult))
                    .collect(Collectors.toList());
        }

        private List<GameResultDTO> build() {
            return gameResults;
        }
    }
}
