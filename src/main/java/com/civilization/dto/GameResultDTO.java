package com.civilization.dto;

import com.civilization.model.UserGameResult;

public class GameResultDTO {
    private Long gameId;
    private String username;
    private UserGameResult gameResult;
    private Long oldRating;
    private Long newRating;

    public GameResultDTO() {
        this(null, null, UserGameResult.NOT_DETECTED);
    }

    public GameResultDTO(Long gameId, String username, UserGameResult gameResult) {
        this.gameId = gameId;
        this.username = username;
        this.gameResult = gameResult;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserGameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(UserGameResult gameResult) {
        this.gameResult = gameResult;
    }

    public Long getOldRating() {
        return oldRating;
    }

    public void setOldRating(Long oldRating) {
        this.oldRating = oldRating;
    }

    public Long getNewRating() {
        return newRating;
    }

    public void setNewRating(Long newRating) {
        this.newRating = newRating;
    }
}
