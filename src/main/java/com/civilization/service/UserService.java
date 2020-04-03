package com.civilization.service;

import com.civilization.dto.GameResultDTO;
import com.civilization.model.User;
import com.civilization.model.UserRank;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    void save(User user);
    List<User> createFFAGameForUsers(List<String> usernames);
    List<GameResultDTO> createFFAReport(List<GameResultDTO> gameResults) throws Exception;
    UserRank findUserRank(String username);
}
