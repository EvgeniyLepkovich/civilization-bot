package com.civilization.repository;

import com.civilization.model.GameResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameResultRepository extends CrudRepository<GameResult, Long> {
}
