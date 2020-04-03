package com.civilization.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.civilization.model.UserActiveGame;

@Repository
public interface UserActiveGameRepository extends CrudRepository<UserActiveGame, Long> {
}
