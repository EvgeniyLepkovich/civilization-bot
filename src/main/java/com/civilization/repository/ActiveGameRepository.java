package com.civilization.repository;

import com.civilization.model.ActiveGame;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveGameRepository extends CrudRepository<ActiveGame, Long> {
}
