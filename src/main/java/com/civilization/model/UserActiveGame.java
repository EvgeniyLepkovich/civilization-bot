package com.civilization.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    public UserActiveGame() {
    }

    public UserActiveGame(User user, ActiveGame activeGame) {
        this.user = user;
        this.activeGame = activeGame;
    }

    private boolean isGameConfirmed;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActiveGame that = (UserActiveGame) o;
        return id == that.id &&
                isGameConfirmed == that.isGameConfirmed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isGameConfirmed);
    }
}
