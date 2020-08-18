package com.civilization.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;

    private String username;
    private Long rating;

    @OneToMany(mappedBy = "user")
    private Set<UserActiveGame> userActiveGames = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<GameResult> gameResults = new HashSet<>();

    public User() {}

    public User(String username, Long rating) {
        this.username = username;
        this.rating = rating;
    }

    public Set<GameResult> getGameResults() {
        return gameResults;
    }

    public void setGameResults(Set<GameResult> gameResults) {
        this.gameResults = gameResults;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
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
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(rating, user.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, rating);
    }
}
