package com.civilization.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_result_id")
    private long id;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private UserGameResult userGameResult;

    private Long oldRating;
    private Long newRating;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "active_game_id")
    private ActiveGame activeGame;

    public GameResult() {
        this(LocalDateTime.now(), null, null, null);
    }

    public GameResult(UserGameResult userGameResult, Long oldRating, Long newRating) {
        this(LocalDateTime.now(), userGameResult, oldRating, newRating);
    }

    public GameResult(LocalDateTime date, UserGameResult userGameResult, Long oldRating, Long newRating) {
        this.date = date;
        this.userGameResult = userGameResult;
        this.oldRating = oldRating;
        this.newRating = newRating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UserGameResult getUserGameResult() {
        return userGameResult;
    }

    public void setUserGameResult(UserGameResult userGameResult) {
        this.userGameResult = userGameResult;
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

    public ActiveGame getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameResult that = (GameResult) o;
        return id == that.id &&
                Objects.equals(date, that.date) &&
                userGameResult == that.userGameResult &&
                Objects.equals(oldRating, that.oldRating) &&
                Objects.equals(newRating, that.newRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, userGameResult, oldRating, newRating);
    }
}
