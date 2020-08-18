package com.civilization.service;

import com.civilization.dto.GameResultDTO;
import com.civilization.dto.UserDTO;
import com.civilization.model.UserRank;

import java.util.List;

public interface DrawTableService {
    String drawGameTable(List<UserDTO> users, Long gameId);
    String drawGameStartedTable(List<UserDTO> users, Long gameId);
    String drawGameFinishedTable(List<GameResultDTO> gameResultDTOs, Long gameId);
    String drawGameDeclinedTable(List<UserDTO> users, Long gameId);
    String drawAllUsersRatingTable(List<UserRank> userRanks);
}
