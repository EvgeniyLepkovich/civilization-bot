package com.civilization.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.civilization.model.ActiveGame;
import com.civilization.model.GameResult;

@Repository
public interface GameResultRepository extends CrudRepository<GameResult, Long> {
}
