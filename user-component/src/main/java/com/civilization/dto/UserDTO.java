package com.civilization.dto;

import com.civilization.model.User;
import com.civilization.model.UserRank;

public class UserDTO {
    private User user;
    private UserRank userRank;

    public UserDTO() {
    }

    public UserDTO(User user, UserRank userRank) {
        this.user = user;
        this.userRank = userRank;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserRank getUserRank() {
        return userRank;
    }

    public void setUserRank(UserRank userRank) {
        this.userRank = userRank;
    }
}
