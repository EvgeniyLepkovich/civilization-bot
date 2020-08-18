package com.civilization.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserNationRoll {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_nation_roll_generator")
    @SequenceGenerator(name = "user_nation_roll_generator", sequenceName = "user_nation_roll_sequence")
    @Column(name = "user_nation_roll_id")
    private long id;

    @OneToMany(mappedBy = "userNationRoll")
    private Set<UserActiveGame> userActiveGame = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_nation_roll_to_nation",
            joinColumns = {
                    @JoinColumn(name = "user_nation_roll_id", referencedColumnName = "user_nation_roll_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "nation_id", referencedColumnName = "nation_id")
            })
    private Set<Nation> nationRolls = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<UserActiveGame> getUserActiveGame() {
        return userActiveGame;
    }

    public void setUserActiveGame(Set<UserActiveGame> userActiveGame) {
        this.userActiveGame = userActiveGame;
    }

    public Set<Nation> getNationRolls() {
        return nationRolls;
    }

    public void setNationRolls(Set<Nation> nationRolls) {
        this.nationRolls = nationRolls;
    }

    public void addNationRoll(Nation roll) {
        this.nationRolls.add(roll);
    }
}
