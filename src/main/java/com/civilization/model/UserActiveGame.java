package com.civilization.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_active_game")
public class UserActiveGame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_active_game_id")
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "active_game_id")
    private ActiveGame activeGame;

    private boolean isGameConfirmed;
    private boolean isGameHost;

    public UserActiveGame() {
    }

    public UserActiveGame(User user, ActiveGame activeGame) {
        this.user = user;
        this.activeGame = activeGame;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ActiveGame getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
    }

    public boolean isGameConfirmed() {
        return isGameConfirmed;
    }

    public void setGameConfirmed(boolean gameConfirmed) {
        isGameConfirmed = gameConfirmed;
    }

    public boolean isGameHost() {
        return isGameHost;
    }

    public void setGameHost(boolean gameHost) {
        isGameHost = gameHost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActiveGame that = (UserActiveGame) o;
        return id == that.id &&
                isGameConfirmed == that.isGameConfirmed &&
                isGameHost == that.isGameHost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isGameConfirmed, isGameHost);
    }
}
