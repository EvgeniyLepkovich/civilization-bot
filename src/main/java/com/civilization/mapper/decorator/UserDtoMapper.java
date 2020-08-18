package com.civilization.mapper.decorator;

import com.civilization.dto.UserDTO;
import com.civilization.model.User;
import com.civilization.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoMapper {
    @Autowired
    private RatingService ratingService;

    public List<UserDTO> toUsersDTO(List<User> usersInGame) {
        return usersInGame.stream()
                .map(user -> new UserDTO(user, ratingService.findUserRank(user.getUsername())))
                .collect(Collectors.toList());
    }
}
