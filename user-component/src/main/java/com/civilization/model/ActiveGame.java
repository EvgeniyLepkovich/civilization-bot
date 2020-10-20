package com.civilization.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class ActiveGame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "active_game_generator")
    @SequenceGenerator(name = "active_game_generator", sequenceName = "active_game_sequence", initialValue = 2500)
    @Column(name = "active_game_id")
    private long id;

    private LocalDateTime startDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus = GameStatus.CREATE;

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

    public Set<UserActiveGame> getUserActiveGames() {
        return userActiveGames;
    }

    public void setUserActiveGames(Set<UserActiveGame> userActiveGames) {
        this.userActiveGames = userActiveGames;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveGame that = (ActiveGame) o;
        return id == that.id &&
                Objects.equals(startDate, that.startDate) &&
                gameStatus == that.gameStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, gameStatus);
    }
}
