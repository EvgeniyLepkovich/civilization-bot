package com.civilization.service.impl;

import com.civilization.model.UserRank;
import com.civilization.repository.RatingRepository;
import com.civilization.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public UserRank findUserRank(String username) {
        return ratingRepository.findUserRank(username);
    }
}
