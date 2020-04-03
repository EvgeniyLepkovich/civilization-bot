package com.civilization.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ActiveGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "active_game_id")
    private long id;
    private boolean isStarted;
    private boolean isReported;
    private LocalDateTime startDate;

    @OneToMany(mappedBy = "activeGame")
    private Set<UserActiveGame> userActiveGames = new HashSet<>();

    @OneToMany(mappedBy = "activeGame")
    private Set<GameResult> gameResult = new HashSet<>();

    public Set<GameResult> getGameResult() {
        return gameResult;
    }

    public void setGameResult(Set<GameResult> gameResult) {
        this.gameResult = gameResult;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public Set<UserActiveGame> getUserActiveGames() {
        return userActiveGames;
    }

    public void setUserActiveGames(Set<UserActiveGame> userActiveGames) {
        this.userActiveGames = userActiveGames;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveGame that = (ActiveGame) o;
        return id == that.id &&
                isStarted == that.isStarted &&
                isReported == that.isReported &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(userActiveGames, that.userActiveGames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isStarted, isReported, startDate, userActiveGames);
    }
}
