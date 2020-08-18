package com.civilization.model;

import javax.persistence.*;

@Entity
public class Nation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nation_generator")
    @SequenceGenerator(name = "nation_generator", sequenceName = "nation_sequence")
    @Column(name = "nation_id")
    private long id;

    private String nation;
    private String emojiCode;
    private boolean active;

    public Nation() {
    }

    public Nation(String nation, String emojiCode) {
        this(nation, emojiCode, true);
    }

    public Nation(String nation, String emojiCode, boolean active) {
        this.nation = nation;
        this.emojiCode = emojiCode;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getEmojiCode() {
        return emojiCode;
    }

    public void setEmojiCode(String emojiCode) {
        this.emojiCode = emojiCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
