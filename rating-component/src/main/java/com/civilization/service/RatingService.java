package com.civilization.service;

import com.civilization.model.UserRank;

public interface RatingService {
    UserRank findUserRank(String username);
}
